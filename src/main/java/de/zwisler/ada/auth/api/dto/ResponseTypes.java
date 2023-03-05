package de.zwisler.ada.auth.api.dto;

public enum ResponseTypes {
  CODE("code"),
  TOKEN("token");


  private final String val;

  ResponseTypes(String code) {
    val = code;
  }

  @Override
  public String toString() {
    return val;
  }
}
