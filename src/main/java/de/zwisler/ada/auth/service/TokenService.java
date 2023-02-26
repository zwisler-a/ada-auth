package de.zwisler.ada.auth.service;

import de.zwisler.ada.auth.domain.AuthTokenPayload;
import de.zwisler.ada.auth.domain.IdTokenPayload;
import de.zwisler.ada.auth.domain.RefreshTokenPayload;
import de.zwisler.ada.auth.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

  final KeySetService keySetService;

  String createAuthToken(AuthTokenPayload payload) {
    return Jwts.builder()
        .claim("type", "auth")
        .claim("name", payload.getUsername())
        .claim("permissions", payload.getPermissions())
        .setSubject(payload.getUserId())
        .setId(UUID.randomUUID().toString())
        .setIssuer("http://localhost:8080")
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(Instant.now().plus(5L, ChronoUnit.HOURS)))
        .signWith(keySetService.getCurrentKeyPair().privateKey())
        .compact();
  }

  String createRefreshToken(RefreshTokenPayload payload) {
    return Jwts.builder()
        .claim("type", "refresh")
        .setSubject(payload.getUserId())
        .setId(UUID.randomUUID().toString())
        .setIssuedAt(Date.from(Instant.now()))
        .signWith(keySetService.getCurrentKeyPair().privateKey())
        .compact();
  }

  String createIdToken(IdTokenPayload payload) {
    return Jwts.builder()
        .claim("type", "id")
        .claim("username", payload.getUsername())
        .setSubject(payload.getUserId())
        .setId(UUID.randomUUID().toString())
        .setIssuedAt(Date.from(Instant.now()))
        .signWith(keySetService.getCurrentKeyPair().privateKey())
        .compact();
  }

  String createAuthorizationCode() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[20];
    random.nextBytes(bytes);
    Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    return encoder.encodeToString(bytes);
  }

  public UUID validateRefreshToken(String refreshToken) {
    var body = Jwts.parserBuilder().setSigningKey(keySetService.getCurrentKeyPair().publicKey()).build()
        .parseClaimsJws(refreshToken).getBody();
    if (!body.get("type").equals("refresh")) {
      throw new UnauthorizedException();
    }
    return UUID.fromString(body.getSubject());
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
