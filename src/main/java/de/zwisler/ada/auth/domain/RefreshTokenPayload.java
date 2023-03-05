package de.zwisler.ada.auth.domain;

import de.zwisler.ada.auth.api.dto.UserDto;
import lombok.Data;

@Data
public class RefreshTokenPayload {

  String userId;
  String nonce;


  public static RefreshTokenPayload from(UserDto userDto, String nonce) {
    RefreshTokenPayload payload = new RefreshTokenPayload();
    payload.userId = userDto.getId();
    payload.nonce = nonce;
    return payload;
  }
}
