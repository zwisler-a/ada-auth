package de.zwisler.ada.auth.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
public class UserEntity {

  @Id
  UUID id = UUID.randomUUID();

  @Column
  String username;
  @Column
  String password;
  @ManyToMany(targetEntity = PermissionEntity.class)
  List<PermissionEntity> permissions;
}
