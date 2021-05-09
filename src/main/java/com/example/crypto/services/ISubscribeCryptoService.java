package com.example.crypto.services;

import java.util.List;
import java.util.Optional;

import com.example.crypto.models.SubscribeCrypto;

public interface ISubscribeCryptoService {
    
    Optional<SubscribeCrypto> findByUserAndCurrency(String user, String currency);

    List<SubscribeCrypto> findByCurrency(String currency);

    List<SubscribeCrypto> findByUsername(String userName);

    void save(SubscribeCrypto sc);

}
