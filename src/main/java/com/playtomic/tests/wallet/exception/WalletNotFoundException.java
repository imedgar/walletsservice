package com.playtomic.tests.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "resource not found")
public class WalletNotFoundException extends RuntimeException {
}
