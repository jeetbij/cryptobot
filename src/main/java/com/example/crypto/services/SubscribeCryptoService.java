package com.example.crypto.services;

import java.util.List;
import java.util.Optional;

import com.example.crypto.models.SubscribeCrypto;
import com.example.crypto.repository.SubscribeCryptoRespository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeCryptoService implements ISubscribeCryptoService {

    @Autowired
    private SubscribeCryptoRespository repository;

    @Override
    public Optional<SubscribeCrypto> findByUserAndCurrency(String user, String currency) {
        return repository.findByUserAndCurrency(user, currency);
    }

    @Override
    public List<SubscribeCrypto> findByCurrency(String currency) {
        return repository.findByCurrency(currency);
    }

    @Override
    public List<SubscribeCrypto> findByUsername(String userName) {
        return repository.findByUsername(userName);
    }

    @Override
    public void save(SubscribeCrypto sc) {
        repository.save(sc);
    }
    
}
