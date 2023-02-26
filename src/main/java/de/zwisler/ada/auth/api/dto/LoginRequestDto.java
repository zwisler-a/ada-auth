package de.zwisler.ada.auth.api.dto;

import lombok.Data;

@Data
public class LoginRequestDto {

  String username;
  String password;
}
