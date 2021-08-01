package com.playtomic.tests.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "not enough balance in the wallet")
public class WalletBalanceException extends RuntimeException {
}
