package com.playtomic.tests.wallet.config;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.h2.server.web.WebServlet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class WalletConfig {

    /**
     * Dev purposes only. Console @ http localhost:8090/console/
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console/*");
        return registration;
    }

    /**
     * Populates data model
     *
     * @param walletRepository walletRepository
     */
    @Bean
    CommandLineRunner initDatabase(WalletRepository walletRepository) {
        return args -> {

            Wallet firstWallet = Wallet.builder()
                    .id(1L)
                    .user("Sam")
                    .balance(new BigDecimal("200.0"))
                    .creditCard("4001020000000009")
                    .build();
            Wallet secondWallet = Wallet.builder()
                    .id(2L)
                    .user("Porter")
                    .balance(new BigDecimal("100.5"))
                    .creditCard("4001020000000010")
                    .build();
            walletRepository.saveAll(Arrays.asList(firstWallet, secondWallet));
        };
    }
}
