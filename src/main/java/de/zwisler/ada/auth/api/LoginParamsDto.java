package de.zwisler.ada.auth.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginParamsDto {

  String responseType;
  String responseMode;
  String clientId;
  String redirectUri;
  String scope;
  String state;
  String nonce;
  String codeChallenge;

  public void setResponse_type(String responseType) {
    this.responseType = responseType;
  }

  public void setCode_challenge(String codeChallenge) {
    this.codeChallenge = codeChallenge;
  }

  public void setResponse_mode(String responseMode) {
    this.responseMode = responseMode;
  }

  public void setClient_id(String clientId) {
    this.clientId = clientId;
  }

  public void setRedirect_uri(String redirectUri) {
    this.redirectUri = redirectUri;
  }
}
