package de.zwisler.ada.auth.api;

import de.zwisler.ada.auth.service.KeySetService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("${auth.jwksEndpoint}")
public class KeySetController {

  final KeySetService keySetService;

  @GetMapping
  Map<String, Object> getKeySet() {
    return keySetService.getCurrentKeySets().toJSONObject();
  }
}
