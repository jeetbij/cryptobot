package com.example.crypto.repository;

import com.example.crypto.models.TelegramMessageResponse;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramMessageResponseRepository extends JpaRepository<TelegramMessageResponse, Long> {

}