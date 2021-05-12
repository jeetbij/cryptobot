package com.example.crypto.service;

import java.util.List;
import java.util.Optional;

import com.example.crypto.model.CryptoCurrency;

public interface ICryptoCurrencyService {
    
    Optional<CryptoCurrency> findByName(String name);

    List<CryptoCurrency> findAll();

    void save(CryptoCurrency cc);

}
