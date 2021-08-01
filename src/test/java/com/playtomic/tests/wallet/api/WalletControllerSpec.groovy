package com.playtomic.tests.wallet.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.playtomic.tests.wallet.model.Wallet
import com.playtomic.tests.wallet.model.payload.RechargePayload
import com.playtomic.tests.wallet.model.payload.SubtractPayload
import com.playtomic.tests.wallet.model.payload.WalletResponse
import com.playtomic.tests.wallet.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.http.HttpStatus.OK
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@WebMvcTest(controllers = [WalletController.class])
class WalletControllerSpec extends Specification {

    @Autowired
    protected MockMvc mvc

    @Autowired
    WalletService walletService

    @Autowired
    ObjectMapper objectMapper

    def "get /wallets/{id} nominal case"() {

        given:
        def expectedResponse = WalletResponse.builder().data(
                Wallet.builder().id(1L).user('Bridges').balance(5.0).build()
        ).build()

        when:
        def results = mvc.perform(get('/wallets/1'))
                .andReturn()
                .response

        then:
        walletService.getWalletById(*_) >> { expectedResponse }

        and:
        results.status == OK.value()
        results.contentType == 'application/json'
        results.contentAsString == "{\"data\":{\"id\":1,\"balance\":5.0,\"user\":\"Bridges\"}}"
    }

    def "post /wallets/{id}/recharge nominal case"() {

        given:
        def payload = RechargePayload.builder().amount(15.0).build()
        def expectedResponse = WalletResponse.builder().data(
                Wallet.builder().id(1L).user('Bridges').balance(5.0).build()
        ).build()

        when:
        def results = mvc.perform(put('/wallets/1/recharge')
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response

        then:
        walletService.rechargeBalanceInCreditCard(*_) >> { expectedResponse }

        and:
        results.status == OK.value()
        results.contentType == 'application/json'
        results.contentAsString == "{\"data\":{\"id\":1,\"balance\":5.0,\"user\":\"Bridges\"}}"
    }


    def "post /wallets/{id}/subtract nominal case"() {

        given:
        def payload = SubtractPayload.builder().amount(15.0).build()
        def expectedResponse = WalletResponse.builder().data(
                Wallet.builder().id(1L).user('Bridges').balance(5.0).build()
        ).build()

        when:
        def results = mvc.perform(put('/wallets/1/subtract')
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response

        then:
        walletService.subtractFromWallet(*_) >> { expectedResponse }

        and:
        results.status == OK.value()
        results.contentType == 'application/json'
        results.contentAsString == "{\"data\":{\"id\":1,\"balance\":5.0,\"user\":\"Bridges\"}}"
    }

    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        WalletService walletService() {
            return detachedMockFactory.Stub(WalletService)
        }
    }
}
