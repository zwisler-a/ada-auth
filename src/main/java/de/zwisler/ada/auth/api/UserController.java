package de.zwisler.ada.auth.api;

import de.zwisler.ada.auth.api.dto.UserDto;
import de.zwisler.ada.auth.exceptions.UserNotFoundException;
import de.zwisler.ada.auth.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

  final UserService userService;

  @GetMapping
  List<UserDto> getUser() {
    return userService.getUsers();
  }

  @PostMapping
  void createUser(@RequestBody UserDto userDto) {
    userService.createUser(userDto);
  }

  @DeleteMapping("{id}")
  void deleteUser(@PathVariable("id") UUID id) {
    try {
      userService.deleteUser(id);
    } catch (EmptyResultDataAccessException e) {
      throw new UserNotFoundException();
    }
  }

}
