package de.zwisler.ada.auth.api;

import de.zwisler.ada.auth.config.AuthConfig;
import de.zwisler.ada.auth.service.KeySetService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping(".well-known/openid-configuration")
public class OpenIDController {

  final AuthConfig authConfig;
  final KeySetService keySetService;

  @GetMapping
  Map<String, Object> getConfig() {

    Map<String, Object> config = new HashMap<>();
    config.put("issuer", authConfig.getBaseUrl());
    config.put("authorization_endpoint", authConfig.getBaseUrl() + authConfig.getAuthorizationEndpoint());
    config.put("token_endpoint", authConfig.getBaseUrl() + authConfig.getTokenEndpoint());
    config.put("jwks_uri", authConfig.getBaseUrl() + authConfig.getJwksEndpoint());

    return config;
  }
}
