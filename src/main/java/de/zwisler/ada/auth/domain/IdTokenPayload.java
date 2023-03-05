package de.zwisler.ada.auth.domain;

import de.zwisler.ada.auth.api.dto.UserDto;
import java.util.List;
import lombok.Data;

@Data
public class IdTokenPayload {

  String userId;
  String username;
  List<String> permissions;
  String nonce;


  public static IdTokenPayload from(UserDto userDto, String nonce) {
    IdTokenPayload payload = new IdTokenPayload();
    payload.userId = userDto.getId();
    payload.username = userDto.getUsername();
    payload.permissions = userDto.getPermissions();
    payload.nonce = nonce;
    return payload;
  }
}
