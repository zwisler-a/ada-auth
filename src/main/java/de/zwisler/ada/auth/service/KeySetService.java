package de.zwisler.ada.auth.service;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import de.zwisler.ada.auth.domain.KeySet;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KeySetService {

  @Value("${auth.key-path}")
  String keyPath;

  KeySet currentKeyPair;

  public KeySet getCurrentKeyPair() {
    if (Objects.isNull(currentKeyPair)) {
      currentKeyPair = generateKeyPair();
    }
    return currentKeyPair;
  }

  KeySet generateKeyPair() {
    KeySet existingKp = this.loadKeys();
    if (Objects.nonNull(existingKp)) {
      return existingKp;
    }
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
    this.saveKeys(publicKey, privateKey);
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

  private void saveKeys(PublicKey publicKey, PrivateKey privateKey) {
    log.info("Saving keys!");
    try {
      new File(keyPath).mkdirs();
    } catch (Exception e) {
      log.error("Error while creating key dir", e);
    }
    try (FileOutputStream fos = new FileOutputStream(keyPath + "key.pub")) {
      fos.write(publicKey.getEncoded());
    } catch (Exception e) {
      log.error("Error while saving public key", e);
    }
    try (FileOutputStream fos = new FileOutputStream(keyPath + "key.key")) {
      fos.write(privateKey.getEncoded());
    } catch (Exception e) {
      log.error("Error while saving private key", e);
    }
  }


  private KeySet loadKeys() {
    log.info("Loading keys!");
    try {
      File publicKeyFile = new File(keyPath + "key.pub");
      byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
      File privateKeyFile = new File(keyPath + "key.key");
      byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());

      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
      PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
      EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
      PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
      return new KeySet("RSA", publicKey, privateKey);
    } catch (Exception e) {
      log.error("Could not load keys ...");
      return null;
    }
  }
}
