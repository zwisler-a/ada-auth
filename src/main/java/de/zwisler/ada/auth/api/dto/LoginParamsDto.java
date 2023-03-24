package de.zwisler.ada.auth.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginParamsDto {

    String responseType;
    String responseMode;
    String clientId;
    String redirectUri;
    String scope;
    String state;
    String nonce;
    String codeChallenge;
}
