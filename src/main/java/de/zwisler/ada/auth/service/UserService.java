package de.zwisler.ada.auth.service;

import de.zwisler.ada.auth.api.dto.UserDto;
import de.zwisler.ada.auth.exceptions.UnauthorizedException;
import de.zwisler.ada.auth.persistence.PermissionRepository;
import de.zwisler.ada.auth.persistence.UserRepository;
import de.zwisler.ada.auth.persistence.entity.UserEntity;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

  final UserRepository userRepository;
  final PermissionRepository permissionRepository;
  final CryptoService cryptoService;

  public boolean createUser(UserDto userDto) {
    Optional<UserEntity> existing = userRepository.findByUsername(userDto.getUsername());
    if (existing.isPresent()) {
      throw new UnauthorizedException();
    }

    UserEntity entity = new UserEntity();
    entity.setUsername(userDto.getUsername());
    entity.setPassword(cryptoService.encode(userDto.getPassword()));
    entity.setPermissions(
        userDto.getPermissions().stream().map(permissionRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList()
    );
    userRepository.save(entity);
    return true;
  }

  Optional<UserDto> getUserById(UUID id) {
    return userRepository.findById(id).map(UserDto::from);
  }

  public List<UserDto> getUsers() {
    return userRepository.findAll().stream().map(UserDto::from).toList();
  }

  @PostConstruct
  public void initialize() {
    if (this.userRepository.findByUsername("admin").isEmpty()) {
      UserDto admin = new UserDto();
      admin.setPassword("admin");
      admin.setUsername("admin");
      admin.setPermissions(List.of());
      log.info("Creating admin user with username: {} and password {}.", admin.getUsername(),
          admin.getPassword());
      this.createUser(admin);
    }
  }

  public void deleteUser(UUID id) {
    userRepository.deleteById(id);
  }
}
