package de.zwisler.ada.auth.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;
import de.zwisler.ada.auth.config.AuthConfig;
import de.zwisler.ada.auth.domain.AccessTokenPayload;
import de.zwisler.ada.auth.domain.IdTokenPayload;
import de.zwisler.ada.auth.domain.RefreshTokenPayload;
import de.zwisler.ada.auth.domain.TokenTypes;
import de.zwisler.ada.auth.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.UUID;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenService {

  final KeySetService keySetService;
  final AuthConfig authConfig;

  String createAuthToken(AccessTokenPayload payload) {
    return createSignedToken(jwtClaimSet(TokenTypes.ACCESS)
        .claim("name", payload.getUsername())
        .claim("permissions", payload.getPermissions())
        .claim("nonce", payload.getNonce())
        .subject(payload.getUserId())
        .expirationTime(Date.from(Instant.now().plus(5L, ChronoUnit.HOURS))));
  }

  String createRefreshToken(RefreshTokenPayload payload) {
    return createSignedToken(jwtClaimSet(TokenTypes.REFRESH)
        .claim("nonce", payload.getNonce())
        .subject(payload.getUserId())
        .expirationTime(Date.from(Instant.now().plus(999L, ChronoUnit.DAYS))));
  }

  String createIdToken(IdTokenPayload payload) {
    return createSignedToken(jwtClaimSet(TokenTypes.ID)
        .claim("nonce", payload.getNonce())
        .claim("username", payload.getUsername())
        .claim("permissions", payload.getPermissions())
        .subject(payload.getUserId())
        .expirationTime(Date.from(Instant.now().plus(90L, ChronoUnit.DAYS))));
  }

  private Builder jwtClaimSet(TokenTypes tokenType) {
    return new JWTClaimsSet.Builder()
        .issuer(authConfig.getBaseUrl())
        .audience("ada")
        .jwtID(UUID.randomUUID().toString())
        .claim("type", tokenType.toString())
        .issueTime(Date.from(Instant.now()));
  }

  @SneakyThrows
  private String createSignedToken(Builder builder) {
    var header = new JWSHeader.Builder(JWSAlgorithm.RS256)
        .type(JOSEObjectType.JWT)
        .build();
    var jwt = new SignedJWT(header, builder.build());
    try {
      jwt.sign(new RSASSASigner(keySetService.getCurrentKeyPair().privateKey()));
      return jwt.serialize();
    } catch (JOSEException e) {
      log.error("Error while signing JWT", e);
      throw new AuthenticationException();
    }
  }

  String createAuthorizationCode() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[20];
    random.nextBytes(bytes);
    Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    return encoder.encodeToString(bytes);
  }

  public Pair<UUID, String> validateRefreshToken(String refreshToken) {
    var body = Jwts.parserBuilder().setSigningKey(keySetService.getCurrentKeyPair().publicKey()).build()
        .parseClaimsJws(refreshToken).getBody();
    if (!body.get("type").equals("refresh")) {
      throw new UnauthorizedException();
    }
    return Pair.of(UUID.fromString(body.getSubject()), body.get("nonce").toString());
  }

  public UUID validateAccessToken(String token) {
    var body = Jwts.parserBuilder().setSigningKey(keySetService.getCurrentKeyPair().publicKey()).build()
        .parseClaimsJws(token).getBody();
    if (!body.get("type").equals("auth")) {
      throw new UnauthorizedException();
    }
    return UUID.fromString(body.getSubject());
  }
}
