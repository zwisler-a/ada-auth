package de.zwisler.ada.auth.api.dto;

public enum GrantTypes {
  authorizationCode("authorization_code"),
  refreshToken("refresh_token");


  private final String val;

  GrantTypes(String code) {
    val = code;
  }

  @Override
  public String toString() {
    return val;
  }
}
