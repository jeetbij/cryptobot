package com.example.crypto.services;

import java.util.List;
import java.util.Optional;

import com.example.crypto.models.CryptoCurrency;

public interface ICryptoCurrencyService {
    
    Optional<CryptoCurrency> findByName(String name);

    List<CryptoCurrency> findAll();

    void save(CryptoCurrency cc);

}
