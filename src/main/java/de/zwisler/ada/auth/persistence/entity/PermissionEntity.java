package de.zwisler.ada.auth.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PermissionEntity {

  @Id
  String id;

  @Column
  String name;
}
