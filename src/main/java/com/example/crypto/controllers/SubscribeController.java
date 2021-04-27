package com.example.crypto.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RestController;

import com.example.crypto.dtos.SubscribeCryptoDTO;
import com.example.crypto.models.SubscribeCrypto;
import com.example.crypto.repository.SubscribeCryptoRespository;
import com.example.crypto.repository.TelegramMessageResponseRepository;
import com.example.crypto.utils.TelegramNotifier;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class SubscribeController {

    private final SubscribeCryptoRespository subscribeCryptoRespository;
    private final TelegramMessageResponseRepository telegramMessageResponseRepository;

    Logger logger = LoggerFactory.getLogger(SubscribeController.class);

    SubscribeController(SubscribeCryptoRespository repository, TelegramMessageResponseRepository tmsrepository) {
        this.subscribeCryptoRespository = repository;
        this.telegramMessageResponseRepository = tmsrepository;
    }

    @PostMapping("/subscribe")
    List<SubscribeCrypto> newSubscribeCrypto(@RequestBody SubscribeCryptoDTO data) {
        
        List<SubscribeCrypto> scl = new ArrayList<>();

        String user = data.getUser();
        String cryptoCurrency = data.getCryptoCurrency();
        Double boughtIn = data.getBoughtIn();
        Double notifyAt = data.getNotifyAt();

        user = user.toLowerCase().strip().replaceAll(" ", "_");

        cryptoCurrency = cryptoCurrency.toLowerCase().strip().replaceAll(" ", "_");
        SubscribeCrypto sc = new SubscribeCrypto();

        SubscribeCrypto sco = subscribeCryptoRespository.findByUserAndCurrency(user, cryptoCurrency);
        if (sco != null) {
            sc = sco;
            sc.setBoughtIn(boughtIn);
            sc.setNotifyAt(notifyAt);
            sc.setActive(true);
        } else {
            logger.info(String.format("%s Crypto Currency Subscription not found for user %s \n", cryptoCurrency, user));
            sc.setUser(user);
            sc.setCryptoCurrency(cryptoCurrency);
            sc.setBoughtIn(boughtIn);
            sc.setNotifyAt(notifyAt);
            sc.setActive(true);
        }

        subscribeCryptoRespository.save(sc);
        scl.add(sc);
        
        try {
            String message = String.format("User: %s, Subscribed to: %s, Bought in: %s, Notify at: %s", user, data.getCryptoCurrency(), boughtIn, notifyAt);
            new TelegramNotifier(telegramMessageResponseRepository, subscribeCryptoRespository).sendMessage(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return scl;
    }

}