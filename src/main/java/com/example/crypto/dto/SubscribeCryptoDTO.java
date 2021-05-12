package com.example.crypto.dto;

public class SubscribeCryptoDTO {
    
    Integer id;

    String user;

    String cryptoCurrency;

    Double boughtIn;

    Double notifyAt;

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

}
