package com.example.crypto.utils;

import java.io.IOException;
import java.lang.InterruptedException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.example.crypto.models.SubscribeCrypto;
import com.example.crypto.models.TelegramMessageResponse;
import com.example.crypto.repository.SubscribeCryptoRespository;
import com.example.crypto.repository.TelegramMessageResponseRepository;


public class TelegramNotifier {

    private final String CRYPTO_BOT_TOKEN = "1721251982:AAGhhBO5RRQb7sCG4hE9yXz2cuB90uPqxe0";
    private final String CRYPTO_UPDATES_GROUP_CHAT_ID = "-1001162020470";

    private final TelegramMessageResponseRepository telegramMessageResponseRepository;
    private final SubscribeCryptoRespository subscribeCryptoRespository;

    public TelegramNotifier(TelegramMessageResponseRepository tmrrepository, SubscribeCryptoRespository screpository) {
        this.telegramMessageResponseRepository = tmrrepository;
        this.subscribeCryptoRespository = screpository;
    }

    public void checkAndNotify(String cc, Double price) {
        List<SubscribeCrypto> scs = subscribeCryptoRespository.findByCurrency(cc);

        System.out.printf("Checking price diffrence for %s and current price %s \n", cc, price);

        for(Integer i=0; i<scs.size(); i++) {
                SubscribeCrypto sc = scs.get(i);
                
                Double expectedIncreaseInPrice = sc.getBoughtIn() + (sc.getBoughtIn()*sc.getNotifyAt())%100;
                System.out.printf("Expected increase in price calculated for %s and current price %s, expected price - %s \n", cc, price, expectedIncreaseInPrice);
                
                Double expectedDecreaseInPrice = sc.getBoughtIn() - (sc.getBoughtIn()*sc.getNotifyAt())%100;
                System.out.printf("Expected decrease in price calculated for %s and current price %s, expected price - %s \n", cc, price, expectedDecreaseInPrice);

                if (expectedIncreaseInPrice <= price || expectedDecreaseInPrice >= price) {
                        String message = String.format("Currency %s has reached expected change, current price: %s", cc, price);
                        try {
                                sendMessage(message);
                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        } 
                }
        }
    }

    public void sendMessage(String message) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();

        UriBuilder builder = UriBuilder
                .fromUri("https://api.telegram.org")
                .path("/{token}/sendMessage")
                .queryParam("chat_id", CRYPTO_UPDATES_GROUP_CHAT_ID)
                .queryParam("text", message);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(builder.build("bot" + CRYPTO_BOT_TOKEN))
                .timeout(Duration.ofSeconds(5))
                .build();

        HttpResponse<String> response = client
          .send(request, HttpResponse.BodyHandlers.ofString());

        TelegramMessageResponse tmr = new TelegramMessageResponse();
        tmr.setResponse(response.body().toString());
        telegramMessageResponseRepository.save(tmr);

    }
}