package de.zwisler.ada.auth.service;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CryptoService {

  Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder();

  public String encode(String s) {
    return argon2PasswordEncoder.encode(s);
  }

  public boolean match(String raw, String enc) {
    return argon2PasswordEncoder.matches(raw, enc);
  }

}
