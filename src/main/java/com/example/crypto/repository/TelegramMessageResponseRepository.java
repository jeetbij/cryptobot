package com.example.crypto.repository;

import com.example.crypto.model.TelegramMessageResponse;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramMessageResponseRepository extends JpaRepository<TelegramMessageResponse, Long> {

}