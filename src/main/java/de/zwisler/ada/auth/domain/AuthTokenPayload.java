package de.zwisler.ada.auth.domain;

import de.zwisler.ada.auth.api.dto.UserDto;
import java.util.List;
import lombok.Data;

@Data
public class AuthTokenPayload {

  String userId;
  String username;
  List<String> permissions;


  public static AuthTokenPayload from(UserDto userDto) {
    AuthTokenPayload payload = new AuthTokenPayload();
    payload.userId = userDto.getId();
    payload.username = userDto.getUsername();
    payload.permissions = userDto.getPermissions();
    return payload;
  }
}
