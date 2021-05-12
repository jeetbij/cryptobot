package com.example.crypto.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.crypto.model.CryptoCurrency;
import com.example.crypto.model.CryptoCurrencyTrail;

public interface ICryptoCurrencyTrailService {
    
    List<CryptoCurrencyTrail> get7DayData(LocalDateTime dateTime, CryptoCurrency cc);

    void save(CryptoCurrencyTrail cc);

}
