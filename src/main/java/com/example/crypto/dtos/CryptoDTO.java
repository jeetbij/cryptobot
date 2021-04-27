package com.example.crypto.dtos;

public class CryptoDTO {
    
    Integer id;

    String name;

    Double price;

    Double change24h;

    String currency;

    Boolean coinswitch;

    String otherdata;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
