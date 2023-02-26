package de.zwisler.ada.auth.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {


  @JsonProperty("access_token")
  String accessToken;
  @JsonProperty("refresh_token")
  String refreshToken;
  @JsonProperty("id_token")
  String idToken;
  @JsonProperty("expires_in")
  int expiresIn;
}
