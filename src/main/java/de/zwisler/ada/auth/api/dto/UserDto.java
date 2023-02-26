package de.zwisler.ada.auth.api.dto;

import de.zwisler.ada.auth.persistence.entity.PermissionEntity;
import de.zwisler.ada.auth.persistence.entity.UserEntity;
import java.util.List;
import lombok.Data;

@Data
public class UserDto {

  String id;

  String username;
  String password;

  List<String> permissions;


  public static UserDto from(UserEntity entity) {
    UserDto dto = new UserDto();
    dto.setId(entity.getId().toString());
    dto.setUsername(entity.getUsername());
    dto.setPermissions(entity.getPermissions().stream().map(PermissionEntity::getId).toList());
    return dto;
  }
}
