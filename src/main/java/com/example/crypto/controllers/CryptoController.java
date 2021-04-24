package com.example.crypto.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.example.crypto.dtos.CryptoDTO;
import com.example.crypto.models.CryptoCurrency;
import com.example.crypto.models.CryptoCurrencyTrail;
import com.example.crypto.repository.CryptoCurrencyRepository;
import com.example.crypto.repository.CryptoCurrencyTrailRepository;
import com.example.crypto.repository.SubscribeCryptoRespository;
import com.example.crypto.repository.TelegramMessageResponseRepository;
import com.example.crypto.utils.TelegramNotifier;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class CryptoController {

    private final CryptoCurrencyRepository cryptoCurrencyRepository;
    private final CryptoCurrencyTrailRepository cryptoCurrencyTrailRepository;
    private final TelegramMessageResponseRepository telegramMessageResponseRepository;
    private final SubscribeCryptoRespository subscribeCryptoRespository;

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

        CryptoCurrency cco = cryptoCurrencyRepository.findByName(name);
        if (cco != null) {
            cc = cco;
        } else {
            System.out.printf("No Crypto Currency found with name %s \n", data.getName());
        }

        cc.setDisplayName(displayName);
        cc.setName(name);
        cc.setPrice(price);
        cc.setChange24h(change24h);
        cc.setCurrency(currency);
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

}