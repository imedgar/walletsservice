package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wallet {

    @Id
    private long id;
    private BigDecimal balance;
    private String user;
    @JsonIgnore
    private String creditCard;

    public void recharge(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void subtract(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
}
