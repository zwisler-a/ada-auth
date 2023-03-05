package de.zwisler.ada.auth.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenRequest {

  @JsonProperty("grant_type")
  String grantType;
  String code;
  @JsonProperty("redirect_uri")
  String redirectUri;
  @JsonProperty("client_id")
  String clientId;

  @JsonProperty("refresh_token")
  String refreshToken;

  @JsonProperty("code_verifier")
  String codeVerifier;

  public void setGrant_type(String grantType) {
    this.grantType = grantType;
  }

  public void setRedirect_uri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public void setClient_id(String clientId) {
    this.clientId = clientId;
  }

  public void setRefresh_token(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public void setCode_verifier(String codeVerifier) {
    this.codeVerifier = codeVerifier;
  }
}
