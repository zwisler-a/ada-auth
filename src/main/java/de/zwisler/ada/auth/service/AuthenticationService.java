package de.zwisler.ada.auth.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.zwisler.ada.auth.api.dto.TokenResponse;
import de.zwisler.ada.auth.domain.AuthTokenPayload;
import de.zwisler.ada.auth.domain.IdTokenPayload;
import de.zwisler.ada.auth.domain.RefreshTokenPayload;
import de.zwisler.ada.auth.exceptions.UnauthorizedException;
import de.zwisler.ada.auth.persistence.UserRepository;
import de.zwisler.ada.auth.persistence.entity.UserEntity;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  final UserRepository userRepository;
  final UserService userService;
  final CryptoService cryptoService;
  final TokenService tokenService;
  Cache<String, UUID> authorizationCodes = CacheBuilder.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .expireAfterAccess(0, TimeUnit.SECONDS)
      .build();

  public Optional<String> getAuthorizationCode(String username, String password, String accessToken) {
    UserEntity user = verifyAuthentication(username, password, accessToken);
    if (Objects.nonNull(user)) {
      String code = tokenService.createAuthorizationCode();
      log.debug("Created authorization code for {}: {}", username, code);
      authorizationCodes.put(code, user.getId());
      return Optional.of(code);
    }
    return Optional.empty();
  }

  public Optional<TokenResponse> getAuthorizationTokens(String username, String password, String accessToken) {
    UserEntity user = verifyAuthentication(username, password, accessToken);
    if (Objects.nonNull(user)) {
      log.debug("Generating tokens for {}", username);
      return this.generateTokens(user.getId());
    }
    return Optional.empty();
  }

  UserEntity verifyAuthentication(String username, String password, String accessToken) {
    if (Objects.nonNull(accessToken)) {
      log.debug("Verifying user {} by access_token", username);
      UUID userId = tokenService.validateAccessToken(accessToken);
      return userRepository.findById(userId).orElseThrow();
    }
    if (Objects.nonNull(username) && Objects.nonNull(password)) {
      log.debug("Verifying user {} by password", username);
      UserEntity user = userRepository.findByUsername(username);
      if (cryptoService.match(password, user.getPassword())) {
        return user;
      }
    }
    throw new UnauthorizedException();
  }

  public Optional<TokenResponse> authenticateWithCode(String code) {
    UUID userId = authorizationCodes.getIfPresent(code);
    if (Objects.nonNull(userId)) {
      return generateTokens(userId);
    }
    return Optional.empty();
  }


  public Optional<TokenResponse> authenticateWithRefreshToken(String refreshToken) {
    UUID id = tokenService.validateRefreshToken(refreshToken);
    return generateTokens(id);
  }


  private Optional<TokenResponse> generateTokens(UUID userId) {
    return userService.getUserById(userId).map(userDto -> new TokenResponse(
            tokenService.createAuthToken(AuthTokenPayload.from(userDto)),
            tokenService.createRefreshToken(RefreshTokenPayload.from(userDto)),
            tokenService.createIdToken(IdTokenPayload.from(userDto)),
            1000 * 60 * 60 * 5
        )
    );
  }
}
