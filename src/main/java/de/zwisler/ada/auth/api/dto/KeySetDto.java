package de.zwisler.ada.auth.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.zwisler.ada.auth.domain.KeySet;
import java.util.Base64;
import lombok.Data;

@Data
public class KeySetDto {

  String kty;
  String use;
  @JsonProperty("key_ops")
  String keyOps;
  String alg;
  String kid;
  String x5u;
  String x5c;
  String x5t;

  @JsonProperty("x5t#S256")
  String x5Thumbprint;

  public static KeySetDto from(KeySet currentKeyPair) {
    KeySetDto keySetDto = new KeySetDto();
    keySetDto.kid = Base64.getEncoder().encodeToString(currentKeyPair.privateKey().getEncoded());
    keySetDto.alg = currentKeyPair.alg();
    return keySetDto;
  }
}
