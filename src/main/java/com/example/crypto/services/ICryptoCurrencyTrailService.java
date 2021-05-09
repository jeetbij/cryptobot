package com.example.crypto.services;

import java.time.LocalDateTime;
import java.util.List;

import com.example.crypto.models.CryptoCurrency;
import com.example.crypto.models.CryptoCurrencyTrail;

public interface ICryptoCurrencyTrailService {
    
    List<CryptoCurrencyTrail> get7DayData(LocalDateTime dateTime, CryptoCurrency cc);

    void save(CryptoCurrencyTrail cc);

}
