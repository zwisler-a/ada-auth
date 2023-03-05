package de.zwisler.ada.auth.api;


import de.zwisler.ada.auth.api.dto.LoginRequestDto;
import de.zwisler.ada.auth.api.dto.TokenResponse;
import de.zwisler.ada.auth.config.AuthConfig;
import de.zwisler.ada.auth.exceptions.UnauthorizedException;
import de.zwisler.ada.auth.service.AuthenticationService;
import de.zwisler.ada.auth.service.VerificationService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

  @PostMapping()
  void login(
      @CookieValue(value = "access_token", required = false) String authorization,
      LoginParamsDto loginParamsDto,
      LoginRequestDto loginRequestDto,
      HttpServletResponse response
  ) throws IOException {
    verificationService.verify(loginParamsDto);
    String redirectUrl = authenticationService.authenticate(loginParamsDto, loginRequestDto, authorization);
    response.sendRedirect(redirectUrl);
  }


}
