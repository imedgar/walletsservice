package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.model.payload.RechargePayload;
import com.playtomic.tests.wallet.model.payload.SubtractPayload;
import com.playtomic.tests.wallet.model.payload.WalletResponse;
import com.playtomic.tests.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("wallets")
public class WalletController {

    private final Logger log = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;

    @Autowired
    public WalletController (WalletService walletService) {
        this.walletService = walletService;
    }

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @GetMapping(value ="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletResponse getWallet(@PathVariable long id) {
        return walletService.getWalletById(id);
    }

    @PutMapping(value = "/{id}/recharge", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletResponse recharge(@PathVariable long id, @RequestBody RechargePayload operation) {
        return walletService.rechargeBalanceInCreditCard(id, operation.getCreditCardNumber(), operation.getAmount());
    }

    @PutMapping(value = "/{id}/subtract", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WalletResponse subtract(@PathVariable long id, @RequestBody SubtractPayload operation) {
        return walletService.subtractFromWallet(id, operation.getAmount());
    }
}
