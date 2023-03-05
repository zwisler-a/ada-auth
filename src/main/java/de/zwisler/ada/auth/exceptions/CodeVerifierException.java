package de.zwisler.ada.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid code verifier")
public class CodeVerifierException extends RuntimeException {
}
