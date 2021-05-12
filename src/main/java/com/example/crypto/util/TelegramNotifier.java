package com.example.crypto.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.InterruptedException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.UriBuilder;

import com.example.crypto.model.CryptoCurrency;
import com.example.crypto.model.SubscribeCrypto;
import com.example.crypto.model.TelegramMessageResponse;
import com.example.crypto.service.BeanUtilService;
import com.example.crypto.service.CryptoCurrencyService;
import com.example.crypto.service.ICryptoCurrencyService;
import com.example.crypto.service.ISubscribeCryptoService;
import com.example.crypto.service.ITelegramMessageResponseService;
import com.example.crypto.service.SubscribeCryptoService;
import com.example.crypto.service.TelegramMessageResponseService;


public class TelegramNotifier {

    private final String CRYPTO_BOT_TOKEN = "1721251982:AAGhhBO5RRQb7sCG4hE9yXz2cuB90uPqxe0";
    private final String CRYPTO_UPDATES_GROUP_CHAT_ID = "-1001162020470";

    private final String PRICE_UPDATE_MESSAGE = "**%s** -\n\nCurrent Price - %s\nChange(24h in percentage) - %s";

    ITelegramMessageResponseService telegramMessageResponseRepository = BeanUtilService.getBean(TelegramMessageResponseService.class);
    ISubscribeCryptoService subscribeCryptoRespository = BeanUtilService.getBean(SubscribeCryptoService.class);
    ICryptoCurrencyService cryptoCurrencyRespository = BeanUtilService.getBean(CryptoCurrencyService.class);

    Logger logger = LoggerFactory.getLogger(TelegramNotifier.class);

    
    public void checkAndNotify(String cc) {
        List<SubscribeCrypto> scs = subscribeCryptoRespository.findByCurrency(cc);
        Optional<CryptoCurrency> cco = cryptoCurrencyRespository.findByName(cc);
        CryptoCurrency cryptoCurrency = cco.get();

        // logger.info(String.format("Checking price diffrence for %s and current price %s \n", cc, price));

        logger.info(String.format("Sending notification for %s and current price %s \n", cryptoCurrency.getDisplayName(), cryptoCurrency.getPrice()));

        for(Integer i=0; i<scs.size(); i++) {
                SubscribeCrypto sc = scs.get(i);
                
                // Double expectedIncreaseInPrice = sc.getBoughtIn() + (sc.getBoughtIn()*sc.getNotifyAt())%100;
                // logger.info(String.format("Expected increase in price calculated for %s and current price %s, expected price - %s \n", cc, price, expectedIncreaseInPrice));
                
                // Double expectedDecreaseInPrice = sc.getBoughtIn() - (sc.getBoughtIn()*sc.getNotifyAt())%100;
                // logger.info(String.format("Expected decrease in price calculated for %s and current price %s, expected price - %s \n", cc, price, expectedDecreaseInPrice));
                if(sc.getTelegramChatId() == null) {
                        continue;
                }
                try {
                        String message = String.format(PRICE_UPDATE_MESSAGE, cryptoCurrency.getDisplayName(), cryptoCurrency.getPrice(), cryptoCurrency.getChange24h());
                        logger.info(String.format("TELEGRAM:: message - %s, username - %s, chatId - %s\n", message, sc.getUser(), sc.getTelegramChatId()));
                        sendMessage(message, sc.getTelegramChatId());
                        // sendWhatsappMessage(message);
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } 
        }
    }

    public void sendMessage(String message, String chatId) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();

        UriBuilder builder = UriBuilder
                .fromUri("https://api.telegram.org")
                .path("/{token}/sendMessage")
                .queryParam("chat_id", chatId)
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

    public void sendWhatsappMessage(String message) throws IOException, InterruptedException {

            
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();

        UriBuilder builder = UriBuilder
                .fromUri("https://api.callmebot.com/whatsapp.php")
                .queryParam("phone", "+917073325643")
                .queryParam("text", message)
                .queryParam("apikey", "679642");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(builder.build())
                .timeout(Duration.ofSeconds(5))
                .build();

        HttpResponse<String> response = client
          .send(request, HttpResponse.BodyHandlers.ofString());

        logger.info(String.format("WHATSAPP:: api response %s", response.body().toString()));

    }
}