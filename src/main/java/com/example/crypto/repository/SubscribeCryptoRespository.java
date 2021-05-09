package com.example.crypto.repository;

import java.util.List;
import java.util.Optional;

import com.example.crypto.models.SubscribeCrypto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SubscribeCryptoRespository extends JpaRepository<SubscribeCrypto, Long> {

    @Query("SELECT sc FROM SubscribeCrypto sc where sc.user = :user and sc.cryptoCurrency = :currency and sc.active = 1")
    Optional<SubscribeCrypto> findByUserAndCurrency(
        @Param("user") String user,
        @Param("currency") String cryptoCurrency
    );

    @Query("SELECT sc FROM SubscribeCrypto sc where sc.cryptoCurrency = :currency and sc.active = 1")
    List<SubscribeCrypto> findByCurrency(
        @Param("currency") String cryptoCurrency
    );

    @Query("SELECT sc FROM SubscribeCrypto sc where sc.user = :userName and sc.active = 1")
    List<SubscribeCrypto> findByUsername(
        @Param("userName") String userName
    );

}