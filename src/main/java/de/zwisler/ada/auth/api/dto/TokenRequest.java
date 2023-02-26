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


}
