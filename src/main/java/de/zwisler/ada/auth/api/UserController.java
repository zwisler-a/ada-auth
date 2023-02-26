package de.zwisler.ada.auth.api;

import de.zwisler.ada.auth.api.dto.UserDto;
import de.zwisler.ada.auth.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

}
