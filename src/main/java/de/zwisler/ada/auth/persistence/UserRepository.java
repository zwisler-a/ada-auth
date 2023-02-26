package de.zwisler.ada.auth.persistence;

import de.zwisler.ada.auth.persistence.entity.UserEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  UserEntity findByUsername(String username);

}

