package com.playtomic.tests.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "there was a problem with a 3rd party platform. Wait until your next request.")
public class ThirdPartyException extends RuntimeException {
}
