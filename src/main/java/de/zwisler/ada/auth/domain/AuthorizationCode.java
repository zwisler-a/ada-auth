package de.zwisler.ada.auth.domain;

import java.util.UUID;
import lombok.Value;

public record AuthorizationCode(
    String code,
    UUID userId,
    String nonce,
    String codeChallenge
) {
}
