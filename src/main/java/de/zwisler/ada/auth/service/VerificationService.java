package de.zwisler.ada.auth.service;

import de.zwisler.ada.auth.api.dto.LoginParamsDto;
import de.zwisler.ada.auth.config.AuthConfig;
import de.zwisler.ada.auth.exceptions.InvalidRedirectException;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {

  final AuthConfig authConfig;

  public void verify(LoginParamsDto loginParamsDto) {
    boolean anyRedirectMatches = !authConfig.getAllowedRedirects().stream()
        .map(Pattern::compile)
        .filter(pattern -> pattern.matcher(loginParamsDto.getRedirectUri()).matches())
        .toList().isEmpty();

    if (!anyRedirectMatches) {
      throw new InvalidRedirectException();
    }

  }

}
