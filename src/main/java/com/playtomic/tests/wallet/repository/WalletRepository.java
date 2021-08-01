package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query("SELECT w FROM Wallet w WHERE w.id = ?1")
    Wallet findWalletById(long id);
}
