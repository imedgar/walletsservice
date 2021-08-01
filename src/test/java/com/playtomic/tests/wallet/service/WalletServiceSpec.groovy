package com.playtomic.tests.wallet.service

import com.playtomic.tests.wallet.WalletApplication
import com.playtomic.tests.wallet.config.WalletConfig
import com.playtomic.tests.wallet.exception.ThirdPartyException
import com.playtomic.tests.wallet.exception.WalletBalanceException
import com.playtomic.tests.wallet.exception.WalletNotFoundException
import com.playtomic.tests.wallet.model.Wallet
import com.playtomic.tests.wallet.model.payload.WalletResponse
import com.playtomic.tests.wallet.repository.WalletRepository
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@ActiveProfiles(['test'])
@ContextConfiguration(classes = [WalletApplication.class, WalletConfig.class])
class WalletServiceSpec extends Specification {

    WalletService walletService
    WalletRepository walletRepository
    StripeService stripeService

    def setup() {
        walletRepository = Mock(WalletRepository)
        stripeService = Mock(StripeService)
        walletService = new WalletService(walletRepository, stripeService)
    }

    def 'get wallet by id returns a wallet - nominal case'() {
        given:
        def wallet = Wallet.builder()
                .id(1L)
                .balance(5.0)
                .user('Bridges')
                .creditCard('123456')
                .build()

        when:
        def result = walletService.getWalletById(1L)

        then:
        1 * walletRepository.findWalletById(*_) >> { arguments ->
            arguments[0] == 1L
            wallet
        }

        and:
        result == WalletResponse.builder().data(wallet).build()
    }

    def 'get wallet by id returns a wallet - throws exception'() {

        when:
        walletService.getWalletById(1L)

        then:
        1 * walletRepository.findWalletById(*_) >> { arguments ->
            arguments[0] == 1L
            null
        }
        thrown(WalletNotFoundException)
    }

    def 'recharge balance in credit card - nominal case'() {
        given:
        def wallet = Wallet.builder()
                .id(1L)
                .balance(5.0)
                .user('Bridges')
                .creditCard('123456')
                .build()
        when:
        def result = walletService.rechargeBalanceInCreditCard(1L, '123456', 1.5)

        then:
        1 * walletRepository.findWalletById(*_) >> { arguments ->
            arguments[0] == 1L
            wallet
        }
        1 * stripeService.charge(*_) >> { arguments ->
            arguments[0] == '123456'
            arguments[1] == 1.5
        }
        1 * walletRepository.save(*_)

        and:
        result == WalletResponse.builder().data(wallet).build()
    }

    def 'recharge balance in credit card - third party platform throws exception'() {
        given:
        def wallet = Wallet.builder()
                .id(1L)
                .balance(5.0)
                .user('Bridges')
                .creditCard('123456')
                .build()

        when:
        walletService.rechargeBalanceInCreditCard(1L, '123456', 20.5)

        then:
        1 * walletRepository.findWalletById(*_) >> { arguments ->
            arguments[0] == 1L
            wallet
        }
        1 * stripeService.charge(*_) >> { throw new StripeServiceException() }
        0 * walletRepository.save(*_)

        and:
        thrown(ThirdPartyException)
    }

    def 'subtract an amount from wallet - nominal case'() {
        given:
        def wallet = Wallet.builder()
                .id(1L)
                .balance(5.0)
                .user('Bridges')
                .creditCard('123456')
                .build()

        when:
        def result = walletService.subtractFromWallet(1L, 1.5)

        then:
        1 * walletRepository.findWalletById(*_) >> { arguments ->
            arguments[0] == 1L
            wallet
        }
        1 * walletRepository.save(*_)

        and:
        result == WalletResponse.builder().data(wallet).build()
    }

    def 'subtract an amount from wallet - not enough balance throws exception'() {
        given:
        def wallet = Wallet.builder()
                .id(1L)
                .balance(5.0)
                .user('Bridges')
                .creditCard('123456')
                .build()

        when:
        walletService.subtractFromWallet(1L, 50.0)

        then:
        1 * walletRepository.findWalletById(*_) >> { arguments ->
            arguments[0] == 1L
            wallet
        }
        0 * walletRepository.save(*_)

        and:
        thrown(WalletBalanceException)
    }
}
