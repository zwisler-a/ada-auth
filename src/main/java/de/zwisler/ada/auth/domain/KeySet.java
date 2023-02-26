package de.zwisler.ada.auth.domain;

import java.security.PrivateKey;
import java.security.PublicKey;

public record KeySet(String alg, PublicKey publicKey, PrivateKey privateKey) {

}
