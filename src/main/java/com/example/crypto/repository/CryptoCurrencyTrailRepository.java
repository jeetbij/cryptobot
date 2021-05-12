package com.example.crypto.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.crypto.model.CryptoCurrency;
import com.example.crypto.model.CryptoCurrencyTrail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CryptoCurrencyTrailRepository extends JpaRepository<CryptoCurrencyTrail, Long> {

    @Query("from CryptoCurrencyTrail cct where cct.createdAt >= ?1 AND cct.cryptoCurrency = ?2")
    List<CryptoCurrencyTrail> get7DayData(LocalDateTime dateTime, CryptoCurrency cc);

}
