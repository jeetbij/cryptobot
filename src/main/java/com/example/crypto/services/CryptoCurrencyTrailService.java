package com.example.crypto.services;

import java.time.LocalDateTime;
import java.util.List;

import com.example.crypto.models.CryptoCurrency;
import com.example.crypto.models.CryptoCurrencyTrail;
import com.example.crypto.repository.CryptoCurrencyTrailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CryptoCurrencyTrailService implements ICryptoCurrencyTrailService {

    @Autowired
    private CryptoCurrencyTrailRepository repository;

    @Override
    public List<CryptoCurrencyTrail> get7DayData(LocalDateTime dateTime, CryptoCurrency cc) {
        return repository.get7DayData(dateTime, cc);
    }

    @Override
    public void save(CryptoCurrencyTrail cc) {
        repository.save(cc);    
    }
    
}
