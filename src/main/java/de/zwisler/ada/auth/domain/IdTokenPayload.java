package de.zwisler.ada.auth.domain;

import de.zwisler.ada.auth.api.dto.UserDto;
import lombok.Data;

@Data
public class IdTokenPayload {

  String userId;
  String username;


  public static IdTokenPayload from(UserDto userDto) {
    IdTokenPayload payload = new IdTokenPayload();
    payload.userId = userDto.getId();
    payload.username = userDto.getUsername();
    return payload;
  }
}
