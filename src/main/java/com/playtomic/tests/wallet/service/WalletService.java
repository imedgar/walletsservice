package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.ThirdPartyException;
import com.playtomic.tests.wallet.exception.WalletBalanceException;
import com.playtomic.tests.wallet.exception.WalletForbiddenException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.helper.ValidationHelper;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.payload.WalletResponse;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final StripeService stripeService;

    private enum Operation {
        RECHARGE, SUBTRACT
    }

    @Autowired
    public WalletService(WalletRepository walletRepository, StripeService stripeService) {
        this.walletRepository = walletRepository;
        this.stripeService = stripeService;
    }

    /**
     * Gets a wallet by id
     * @param id of the wallet
     * @return walletResponse
     */
    public WalletResponse getWalletById(long id) {
        Wallet wallet = queryWallet(id);
        return WalletResponse.builder().data(wallet).build();
    }

    /**
     * Recharge money in that wallet using a credit card number. It charges money in the wallet through a third-party
     * platform
     * @param walletId id of the wallet
     * @param creditCardNumber credit card number
     * @param amount amount to recharge
     * @return OperationResponse
     */
    public WalletResponse rechargeBalanceInCreditCard(long walletId, String creditCardNumber, BigDecimal amount) {

        Wallet wallet = queryWallet(walletId);

        if (wallet.getCreditCard() == null || !wallet.getCreditCard().contentEquals(creditCardNumber)) {
            throw new WalletForbiddenException();
        }

        try {
            stripeService.charge(creditCardNumber, amount);
        } catch (StripeServiceException e) {
            throw new ThirdPartyException();
        }

        performOperation(Operation.RECHARGE, amount, wallet);

        return WalletResponse.builder().data(wallet).build();
    }

    /**
     * Subtract an amount from a wallet (that is, make a charge in that wallet).
     * @param walletId id of the wallet
     * @param amount amount of the charge
     * @return OperationResponse
     */
    public WalletResponse subtractFromWallet(long walletId, BigDecimal amount) {

        Wallet wallet = queryWallet(walletId);

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new WalletBalanceException();
        }

        performOperation(Operation.SUBTRACT, amount, wallet);

        return WalletResponse.builder().data(wallet).build();
    }

    /**
     * Query wallet in the db by its id and checks id
     * @param id of the wallet
     * @return wallet
     */
    private Wallet queryWallet(long id) {
        ValidationHelper.checkPositive("id", id);

        Wallet wallet = walletRepository.findWalletById(id);
        if (wallet == null) {
            throw new WalletNotFoundException();
        }
        return wallet;
    }

    /**
     * Performs an operation that can be RECHARGE or SUBTRACT (charge)
     * @param operation operation to perform
     * @param amount amount
     * @param wallet wallet to perform the operation onto
     */
    private void performOperation(Operation operation, BigDecimal amount, Wallet wallet) {

        if (operation.equals(Operation.RECHARGE)) {
            wallet.recharge(amount);
        } else {
            wallet.subtract(amount);
        }
        walletRepository.save(wallet);
    }
}
