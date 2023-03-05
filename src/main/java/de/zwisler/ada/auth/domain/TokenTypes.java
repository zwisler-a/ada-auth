package de.zwisler.ada.auth.domain;

public enum TokenTypes {
  ACCESS("access"),
  ID("id"),
  REFRESH("refresh");

  private final String s;

  TokenTypes(String s) {
    this.s = s;
  }

  @Override
  public String toString() {
    return s;
  }
}
