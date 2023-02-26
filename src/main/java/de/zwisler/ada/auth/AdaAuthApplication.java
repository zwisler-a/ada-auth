package de.zwisler.ada.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class AdaAuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(AdaAuthApplication.class, args);
  }

}
