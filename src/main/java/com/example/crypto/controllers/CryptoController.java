package com.example.crypto.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import com.example.crypto.utils.TelegramNotifier;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class CryptoController {

    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final CryptoCurrencyTrailRepository cryptoCurrencyTrailRepository;
    private final TelegramMessageResponseRepository telegramMessageResponseRepository;
    private final SubscribeCryptoRespository subscribeCryptoRespository;

    Logger logger = LoggerFactory.getLogger(CryptoController.class);

    CryptoController(CryptoCurrencyRepository ccrepository, CryptoCurrencyTrailRepository cctrepository,
        TelegramMessageResponseRepository tmrrepository, SubscribeCryptoRespository screpository) {
        this.cryptoCurrencyRepository = ccrepository;
        this.cryptoCurrencyTrailRepository = cctrepository;
        this.telegramMessageResponseRepository = tmrrepository;
        this.subscribeCryptoRespository = screpository;
    }

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

        CryptoCurrency cco = cryptoCurrencyRepository.findByName(name);
        if (cco != null) {
            cc = cco;
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

        new TelegramNotifier(telegramMessageResponseRepository, subscribeCryptoRespository).checkAndNotify(
            name,
            price
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
        
        CryptoCurrency cc = cryptoCurrencyRepository.findByName(name);
        List<CryptoCurrencyTrail> cryptos = cryptoCurrencyTrailRepository.get7DayData(today.minusDays(7), cc);

        PriceSummaryDTO ps = new PriceSummaryDTO();
        ps.setName(cc.getName());
        ps.setDisplayName(cc.getDisplayName());
        ps.setChange24h(cc.getChange24h());
        ps.setPriceHistory(cryptos);

        return ps;
    }

}