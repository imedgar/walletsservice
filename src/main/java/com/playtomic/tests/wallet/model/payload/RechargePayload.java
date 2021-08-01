package com.playtomic.tests.wallet.model.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RechargePayload extends OperationPayload {

    private String creditCardNumber;
}
