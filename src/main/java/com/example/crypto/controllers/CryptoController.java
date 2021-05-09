package com.example.crypto.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RestController;

import com.example.crypto.dtos.CryptoDTO;
import com.example.crypto.dtos.PriceSummaryDTO;
import com.example.crypto.models.CryptoCurrency;
import com.example.crypto.models.CryptoCurrencyTrail;
import com.example.crypto.repository.CryptoCurrencyRepository;
import com.example.crypto.repository.CryptoCurrencyTrailRepository;
import com.example.crypto.repository.SubscribeCryptoRespository;
import com.example.crypto.repository.TelegramMessageResponseRepository;
import com.example.crypto.services.ICryptoCurrencyService;
import com.example.crypto.services.ICryptoCurrencyTrailService;
import com.example.crypto.services.ISubscribeCryptoService;
import com.example.crypto.services.ITelegramMessageResponseService;
import com.example.crypto.utils.TelegramNotifier;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class CryptoController {

    @Autowired
    private ICryptoCurrencyService cryptoCurrencyRepository;
    @Autowired
    private ICryptoCurrencyTrailService cryptoCurrencyTrailRepository;

    Logger logger = LoggerFactory.getLogger(CryptoController.class);

    @RequestMapping("/")
	public String index() {
		return "Greetings from Crypto!";
	}

    @PostMapping("/create")
    CryptoCurrency newCryptoCurrency(@RequestBody CryptoDTO data) {
        CryptoCurrencyTrail cct = new CryptoCurrencyTrail();
        CryptoCurrency cc = new CryptoCurrency();

        String displayName = data.getName();
        String name = displayName;
        name = name.toLowerCase().strip();
        name = name.replaceAll(" ", "_");
        Double price = data.getPrice();
        Double change24h = data.getChange24h();
        String currency = data.getCurrency();
        Boolean coinswitch = data.getCoinswitch();
        String otherdata = data.getOtherdata();

        Optional<CryptoCurrency> cco = cryptoCurrencyRepository.findByName(name);
        if (!cco.isEmpty()) {
            cc = cco.get();
        } else {
            logger.info(String.format("No Crypto Currency found with name %s \n", data.getName()));
        }

        cc.setDisplayName(displayName);
        cc.setName(name);
        cc.setPrice(price);
        cc.setChange24h(change24h);
        cc.setCurrency(currency);
        cc.setCoinswitch(coinswitch);
        cc.setOtherdata(otherdata);
        cryptoCurrencyRepository.save(cc);

        cct.setCryptoCurrency(cc);
        cct.setPrice(price);
        cct.setCurrency(currency);
        cryptoCurrencyTrailRepository.save(cct);

        new TelegramNotifier().checkAndNotify(
            name
        );

        return cc;
    }

    @GetMapping("/all")
    List<CryptoCurrency> all() {
        return cryptoCurrencyRepository.findAll();
    }

    @GetMapping("/price-history/{name}")
    PriceSummaryDTO priceHistory(@PathVariable String name) {
        LocalDateTime today = LocalDateTime.now();
        
        Optional<CryptoCurrency> cc = cryptoCurrencyRepository.findByName(name);
        PriceSummaryDTO ps = new PriceSummaryDTO();
        if (!cc.isEmpty()) {
            CryptoCurrency cco = cc.get();
            List<CryptoCurrencyTrail> cryptos = cryptoCurrencyTrailRepository.get7DayData(today.minusDays(7), cco);

            ps.setName(cco.getName());
            ps.setDisplayName(cco.getDisplayName());
            ps.setChange24h(cco.getChange24h());
            ps.setPriceHistory(cryptos);

        }
        return ps;        
    }

}