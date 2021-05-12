package com.example.crypto.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.crypto.model.CryptoCurrencyTrail;

public class PriceSummaryDTO {
    
    String name;

    String displayName;

    Double change24h;

    List<Double> priceHistory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getChange24h() {
        return change24h;
    }

    public void setChange24h(Double change24h) {
        this.change24h = change24h;
    }

    public List<Double> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<CryptoCurrencyTrail> cryptos) {
        List<Double> ph = new ArrayList<>();
        for (CryptoCurrencyTrail crypto : cryptos) {
            ph.add(crypto.getPrice());
        }
        this.priceHistory = ph;
    }
}
