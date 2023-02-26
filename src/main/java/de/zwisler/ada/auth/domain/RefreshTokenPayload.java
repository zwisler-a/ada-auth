package de.zwisler.ada.auth.domain;

import de.zwisler.ada.auth.api.dto.UserDto;
import lombok.Data;

@Data
public class RefreshTokenPayload {

  String userId;


  public static RefreshTokenPayload from(UserDto userDto) {
    RefreshTokenPayload payload = new RefreshTokenPayload();
    payload.userId = userDto.getId();
    return payload;
  }
}
