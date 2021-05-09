package com.example.crypto.services;

import java.util.List;
import java.util.Optional;

import com.example.crypto.models.CryptoCurrency;
import com.example.crypto.repository.CryptoCurrencyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CryptoCurrencyService implements ICryptoCurrencyService {
    
    @Autowired
    private CryptoCurrencyRepository repository;

    @Override
    public Optional<CryptoCurrency> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public void save(CryptoCurrency cc) {
        repository.save(cc);
    }

    @Override
    public List<CryptoCurrency> findAll() {
        return repository.findAll();
    }

}
