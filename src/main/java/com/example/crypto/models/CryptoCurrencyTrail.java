package com.example.crypto.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "crypto_currency_trail")
public class CryptoCurrencyTrail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @ManyToOne
  @JoinColumn(name="crypto_currency_id")
  private CryptoCurrency cryptoCurrency;

  @NotNull
  private Double price;

  @NotNull
  private String currency;

  private final LocalDateTime createdAt = LocalDateTime.now();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public CryptoCurrency getCryptoCurrency() {
    return cryptoCurrency;
  }

  public void setCryptoCurrency(CryptoCurrency cryptoCurrency) {
    this.cryptoCurrency = cryptoCurrency;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public LocalDateTime getCreatedAt() {
      return createdAt;
  }
}
