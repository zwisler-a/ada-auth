package de.zwisler.ada.auth.domain;

import de.zwisler.ada.auth.api.dto.UserDto;
import java.util.List;
import lombok.Data;

@Data
public class AccessTokenPayload {

  String userId;
  String username;
  String nonce;
  List<String> permissions;


  public static AccessTokenPayload from(UserDto userDto, String nonce) {
    AccessTokenPayload payload = new AccessTokenPayload();
    payload.userId = userDto.getId();
    payload.username = userDto.getUsername();
    payload.permissions = userDto.getPermissions();
    payload.nonce = nonce;
    return payload;
  }
}
