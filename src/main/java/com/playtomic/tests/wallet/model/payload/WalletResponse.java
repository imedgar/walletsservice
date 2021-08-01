package com.playtomic.tests.wallet.model.payload;

import com.playtomic.tests.wallet.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WalletResponse {
    private Wallet data;
}
