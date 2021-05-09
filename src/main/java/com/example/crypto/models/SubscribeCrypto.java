package com.example.crypto.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.NotNull;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "subscribe_crypto")
public class SubscribeCrypto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  private String user;

  private String cryptoCurrency;

  private Boolean active;

  private Double boughtIn;

  private Double notifyAt;

  @Column(columnDefinition="TEXT")
  private String telegramChatId;

  private LocalDateTime updatedAt = LocalDateTime.now();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getCryptoCurrency() {
    return cryptoCurrency;
  }

  public void setCryptoCurrency(String cryptoCurrency) {
    this.cryptoCurrency = cryptoCurrency;
  }

  public Double getBoughtIn() {
    return boughtIn;
  }

  public void setBoughtIn(Double boughtIn) {
    this.boughtIn = boughtIn;
  }

  public Double getNotifyAt() {
    return notifyAt;
  }

  public void setNotifyAt(Double notifyAt) {
    this.notifyAt = notifyAt;
  }

  public String getTelegramChatId() {
    return telegramChatId;
  }

  public void setTelegramChatId(String chatId) {
    this.telegramChatId = chatId;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

}
