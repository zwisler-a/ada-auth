package de.zwisler.ada.auth.domain;

import lombok.Builder;
import org.springframework.web.util.UriComponentsBuilder;

@Builder
public class AuthenticationRedirect {

  String code;
  String state;
  String nonce;
  String redirectUri;
  String accessToken;
  String refreshToken;

  public String createRedirect() {
    return UriComponentsBuilder.fromUriString(redirectUri)
        .queryParam("code", code)
        .queryParam("state", state)
        .queryParam("nonce", nonce)
        .queryParam("access_token", accessToken)
        .queryParam("refresh_token", refreshToken)
        .build().toString();
  }
}
