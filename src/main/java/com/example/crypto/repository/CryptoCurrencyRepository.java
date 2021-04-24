package com.example.crypto.repository;

import com.example.crypto.models.CryptoCurrency;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {

    @Query("from CryptoCurrency cc where cc.name = ?1")
    CryptoCurrency findByName(String name);

}