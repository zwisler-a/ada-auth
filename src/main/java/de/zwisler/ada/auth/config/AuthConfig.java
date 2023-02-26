package de.zwisler.ada.auth.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthConfig {

  String baseUrl;
  String authorizationEndpoint;
  String tokenEndpoint;
  String jwksEndpoint;
  List<String> allowedRedirects;
}
