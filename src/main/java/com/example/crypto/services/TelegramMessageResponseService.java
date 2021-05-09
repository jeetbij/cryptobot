package com.example.crypto.services;

import com.example.crypto.models.TelegramMessageResponse;
import com.example.crypto.repository.TelegramMessageResponseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramMessageResponseService implements ITelegramMessageResponseService {

    @Autowired
    TelegramMessageResponseRepository repository;

    @Override
    public void save(TelegramMessageResponse tmr) {
        repository.save(tmr);
    }
    
}
