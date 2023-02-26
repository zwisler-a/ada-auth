package de.zwisler.ada.auth.persistence;

import de.zwisler.ada.auth.persistence.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {
}
