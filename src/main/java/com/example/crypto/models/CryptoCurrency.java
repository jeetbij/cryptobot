package com.example.crypto.models;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import javax.validation.constraints.NotNull;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "crypto_currency")
public class CryptoCurrency {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  private String name;

  @NotNull
  private String displayName;

  @NotNull
  private Double price;

  @NotNull
  private Double change24h;

  @NotNull
  private String currency;

  private Boolean coinswitch;

  @Column(columnDefinition="TEXT")
  private String otherdata;

  @OneToMany(mappedBy = "cryptoCurrency")
  private List<CryptoCurrencyTrail> cryptoCurrencyTrail = new ArrayList<>();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Double getChange24h() {
    return change24h;
  }

  public void setChange24h(Double change24h) {
    this.change24h = change24h;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Boolean getCoinswitch() {
    return coinswitch;
  }

  public void setCoinswitch(Boolean coinswitch) {
    this.coinswitch = coinswitch;
  }

  public String getOtherdata() {
    return otherdata;
  }

  public void setOtherdata(String otherdata) {
    this.otherdata = otherdata;
  }
}
