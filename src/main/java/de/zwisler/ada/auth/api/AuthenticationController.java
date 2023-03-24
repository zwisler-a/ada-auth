package de.zwisler.ada.auth.api;


import de.zwisler.ada.auth.api.dto.LoginParamsDto;
import de.zwisler.ada.auth.api.dto.LoginRequestDto;
import de.zwisler.ada.auth.config.AuthConfig;
import de.zwisler.ada.auth.service.AuthenticationService;
import de.zwisler.ada.auth.service.VerificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("${auth.authorizationEndpoint}")
public class AuthenticationController {

  final AuthenticationService authenticationService;
  final AuthConfig authConfig;
  final VerificationService verificationService;

  @GetMapping
  ModelAndView getLoginPage() {
    return new ModelAndView("index");
  }

  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  void login(
      @CookieValue(value = "access_token", required = false) String authorization,
      @RequestParam(value = "response_type") String responseType,
      @RequestParam(value = "response_mode", required = false) String responseMode,
      @RequestParam(value = "client_id", required = false) String clientId,
      @RequestParam(value = "redirect_uri") String redirectUri,
      @RequestParam(value = "scope") String scope,
      @RequestParam(value = "state", required = false) String state,
      @RequestParam(value = "nonce", required = false) String nonce,
      @RequestParam(value = "code_challenge", required = false) String codeChallenge,
      @ModelAttribute LoginRequestDto loginRequestDto,
      HttpServletResponse response
  ) throws IOException {
    LoginParamsDto loginParamsDto = LoginParamsDto.builder()
        .responseType(responseType)
        .responseMode(responseMode)
        .clientId(clientId)
        .redirectUri(redirectUri)
        .scope(scope)
        .state(state)
        .nonce(nonce)
        .codeChallenge(codeChallenge)
        .build();
    verificationService.verify(loginParamsDto);
    String redirectUrl = authenticationService.authenticate(loginParamsDto, loginRequestDto,
        authorization);
    response.sendRedirect(redirectUrl);
  }


}
