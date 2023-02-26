package de.zwisler.ada.auth.api;


import de.zwisler.ada.auth.api.dto.LoginRequestDto;
import de.zwisler.ada.auth.api.dto.TokenResponse;
import de.zwisler.ada.auth.config.AuthConfig;
import de.zwisler.ada.auth.exceptions.UnauthorizedException;
import de.zwisler.ada.auth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("${auth.authorizationEndpoint}")
public class AuthenticationController {

  final AuthenticationService authenticationService;
  final AuthConfig authConfig;

  @GetMapping
  ModelAndView getLoginPage() {
    return new ModelAndView("index");
  }

  @PostMapping()
  void login(
      @CookieValue(value = "access_token") String authorization,
      @RequestParam(value = "response_type") String responseType,
      @RequestParam(value = "response_mode", required = false) String responseMode,
      @RequestParam(value = "client_id") String clientId,
      @RequestParam(value = "redirect_uri") String redirectUri,
      @RequestParam(value = "scope") String scope,
      @RequestParam(value = "state") String state,
      LoginRequestDto loginRequestDto,
      HttpServletResponse response
  ) throws IOException {
    // http://localhost:8080/auth/login?response_type=autorization_code&client_id=1&redirect_uri=htttp%3A%2F%2Flocalhost%3A3000%2F&scope=o&state=a
    if (authConfig.getAllowedRedirects().contains(redirectUri)) {
      if (responseType.equals("code")) {
        String code = authenticationService.getAuthorizationCode(
            loginRequestDto.getUsername(),
            loginRequestDto.getPassword(),
            authorization
        ).orElseThrow(UnauthorizedException::new);
        response.sendRedirect(redirectUri + "?code=" + code + "&state=" + state);
        return;
      } else if (responseType.equals("token")) {
        TokenResponse tokens = authenticationService.getAuthorizationTokens(
            loginRequestDto.getUsername(),
            loginRequestDto.getPassword(),
            authorization
        ).orElseThrow(UnauthorizedException::new);
        response.sendRedirect(
            redirectUri + "?access_token=" + tokens.getAccessToken() + "&refresh_token=" + tokens.getRefreshToken()
                + "&state="
                + state);
        return;
      }
    }
    throw new UnauthorizedException();

  }


}
