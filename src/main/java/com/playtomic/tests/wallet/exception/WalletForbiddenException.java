package com.playtomic.tests.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "dont have access for the requested resource")
public class WalletForbiddenException extends RuntimeException {
}
