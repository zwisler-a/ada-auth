package de.zwisler.ada.auth.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.Hashing;
import de.zwisler.ada.auth.api.dto.LoginParamsDto;
import de.zwisler.ada.auth.api.dto.LoginRequestDto;
import de.zwisler.ada.auth.api.dto.ResponseTypes;
import de.zwisler.ada.auth.api.dto.TokenResponse;
import de.zwisler.ada.auth.domain.*;
import de.zwisler.ada.auth.exceptions.CodeVerifierException;
import de.zwisler.ada.auth.exceptions.UnauthorizedException;
import de.zwisler.ada.auth.persistence.UserRepository;
import de.zwisler.ada.auth.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  final UserRepository userRepository;
  final UserService userService;
  final CryptoService cryptoService;
  final TokenService tokenService;
  Cache<String, AuthorizationCode> authorizationCodes = CacheBuilder.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .expireAfterAccess(1, TimeUnit.SECONDS)
      .build();

  public String createAuthorizationCode(
      UserEntity user,
      String nonce,
      String codeChallenge) {
    String code = tokenService.createAuthorizationCode();
    log.debug("Created authorization code for {}: {}", user.getUsername(), code);
    authorizationCodes.put(code, new AuthorizationCode(code, user.getId(), nonce, codeChallenge));
    return code;
  }

  UserEntity verifyAuthentication(String username, String password, String accessToken) {
    if (Objects.nonNull(accessToken)) {
      log.debug("Verifying user {} by access_token", username);
      UUID userId = tokenService.validateAccessToken(accessToken);
      return userRepository.findById(userId).orElseThrow(UnauthorizedException::new);
    }
    if (Objects.nonNull(username) && Objects.nonNull(password)) {
      log.debug("Verifying user {} by password", username);
      UserEntity user = userRepository.findByUsername(username)
          .orElseThrow(UnauthorizedException::new);
      if (cryptoService.match(password, user.getPassword())) {
        return user;
      }
    }
    throw new UnauthorizedException();
  }

  public Optional<TokenResponse> authenticateWithCode(String code, String codeVerifier) {
    AuthorizationCode user = authorizationCodes.getIfPresent(code);
    log.debug("Authenticate with code {}", code);
    if (Objects.nonNull(user)) {
      log.debug("Generating access token for {}", user.userId());
      authorizationCodes.invalidate(code);
      String calcChallenge = Base64.getUrlEncoder().encodeToString(
          Hashing.sha256().hashString(codeVerifier, StandardCharsets.UTF_8).asBytes()
      );
      log.debug("{} {}", codeVerifier, calcChallenge);
      if (user.codeChallenge().equals(calcChallenge)) {
        throw new CodeVerifierException();
      }
      return generateTokens(user.userId(), user.nonce());
    }
    return Optional.empty();
  }


  public Optional<TokenResponse> authenticateWithRefreshToken(String refreshToken) {
    Pair<UUID, String> id = tokenService.validateRefreshToken(refreshToken);
    return generateTokens(id.getFirst(), id.getSecond());
  }


  private Optional<TokenResponse> generateTokens(UUID userId, String nonce) {
    return userService.getUserById(userId).map(userDto -> new TokenResponse(
            tokenService.createAuthToken(AccessTokenPayload.from(userDto, nonce)),
            tokenService.createRefreshToken(RefreshTokenPayload.from(userDto, nonce)),
            tokenService.createIdToken(IdTokenPayload.from(userDto, nonce)),
            1000 * 60 * 60 * 5
        )
    );
  }

  public String authenticate(LoginParamsDto params, LoginRequestDto req, String authCookie) {
    UserEntity user = verifyAuthentication(req.getUsername(), req.getPassword(), authCookie);

    if (params.getResponseType().equals(ResponseTypes.CODE.toString())) {
      String code = this.createAuthorizationCode(user, params.getNonce(),
          params.getCodeChallenge());
      return AuthenticationRedirect.builder()
          .redirectUri(params.getRedirectUri())
          .state(params.getState())
          .code(code)
          .nonce(params.getNonce())
          .build().createRedirect();
    }

    if (params.getResponseType().equals(ResponseTypes.TOKEN.toString())) {
      TokenResponse tokenResponse = generateTokens(user.getId(), params.getNonce())
          .orElseThrow(UnauthorizedException::new);
      return AuthenticationRedirect.builder()
          .redirectUri(params.getRedirectUri())
          .state(params.getState())
          .accessToken(tokenResponse.getAccessToken())
          .refreshToken(tokenResponse.getRefreshToken())
          .nonce(params.getNonce())
          .build().createRedirect();
    }
    return "";
  }
}
