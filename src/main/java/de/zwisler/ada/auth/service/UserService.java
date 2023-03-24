package de.zwisler.ada.auth.service;

import de.zwisler.ada.auth.api.dto.UserDto;
import de.zwisler.ada.auth.exceptions.UnauthorizedException;
import de.zwisler.ada.auth.persistence.PermissionRepository;
import de.zwisler.ada.auth.persistence.UserRepository;
import de.zwisler.ada.auth.persistence.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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
}
