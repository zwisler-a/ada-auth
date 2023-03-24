package de.zwisler.ada.auth.api;

import de.zwisler.ada.auth.api.dto.GrantTypes;
import de.zwisler.ada.auth.api.dto.TokenRequest;
import de.zwisler.ada.auth.api.dto.TokenResponse;
import de.zwisler.ada.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping
public class TokenController {

  final AuthenticationService authenticationService;

  @PostMapping(value = "${auth.tokenEndpoint}", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  TokenResponse getToken(@ModelAttribute TokenRequest tokenRequest) {
    if (tokenRequest.getGrantType().equals(GrantTypes.authorizationCode.toString())) {
      return authenticationService.authenticateWithCode(
          tokenRequest.getCode(),
          tokenRequest.getCodeVerifier()
      ).orElseThrow();
    }
    if (tokenRequest.getGrantType().equals(GrantTypes.refreshToken.toString())) {
      return authenticationService.authenticateWithRefreshToken(tokenRequest.getRefreshToken()).orElseThrow();
    }
    return null;
  }


}
