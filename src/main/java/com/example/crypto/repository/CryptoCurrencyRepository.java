package com.example.crypto.repository;

import java.util.Optional;

import com.example.crypto.models.CryptoCurrency;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {

    @Query("from CryptoCurrency cc where cc.name = ?1")
    Optional<CryptoCurrency> findByName(String name);

}