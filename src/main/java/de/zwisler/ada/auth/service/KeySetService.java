package de.zwisler.ada.auth.service;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import de.zwisler.ada.auth.domain.KeySet;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class KeySetService {

  KeySet currentKeyPair;

  public KeySet getCurrentKeyPair() {
    if (Objects.isNull(currentKeyPair)) {
      currentKeyPair = generateKeyPair();
    }
    return currentKeyPair;
  }

  KeySet generateKeyPair() {
    KeyPairGenerator keyGenerator;
    try {
      keyGenerator = KeyPairGenerator.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    keyGenerator.initialize(2048);

    KeyPair kp = keyGenerator.genKeyPair();
    PublicKey publicKey = kp.getPublic();
    PrivateKey privateKey = kp.getPrivate();
    return new KeySet("RSA", publicKey, privateKey);
  }


  public JWKSet getCurrentKeySets() {
    return new JWKSet(new RSAKey.Builder((RSAPublicKey) getCurrentKeyPair().publicKey())
        .privateKey(getCurrentKeyPair().privateKey())
        .keyUse(KeyUse.SIGNATURE)
        .keyID(UUID.randomUUID().toString())
        .issueTime(new Date())
        .build());
  }
}
