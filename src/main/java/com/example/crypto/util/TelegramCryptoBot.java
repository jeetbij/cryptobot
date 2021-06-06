package com.example.crypto.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.crypto.model.CryptoCurrency;
import com.example.crypto.model.SubscribeCrypto;
import com.example.crypto.service.BeanUtilService;
import com.example.crypto.service.CryptoCurrencyService;
import com.example.crypto.service.ICryptoCurrencyService;
import com.example.crypto.service.ISubscribeCryptoService;
import com.example.crypto.service.SubscribeCryptoService;

import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramCryptoBot extends TelegramLongPollingBot {

    //Bot information
    private final String CRYPTO_BOT_TOKEN = "1721251982:AAGhhBO5RRQb7sCG4hE9yXz2cuB90uPqxe0";
    // private final String CRYPTO_BOT_TOKEN = "1729919631:AAExAz2WRfbmWMFsNzRfBZBsHxXD9KDlCMc"; //test
    private final String CRYPTO_BOT_NAME = "CryptoBot";

    //Commands
    private final String START_COMMAND = "start";
    private final String AVAILABLE_CURRENCY_COMMAND = "all";
    private final String HELP_COMMAND = "help";
    private final String SUBSCRIBE_COMMAND = "subscribe";
    private final String UNSUBSCRIBE_COMMAND = "unsubscribe";
    private final String LIST_SUBSCRIPTION_COMMAND = "list";

    //Messages
    private final String START_MESSAGE = "Hi! This bot updates you about cryptocurrency prices.\n\nTo see what this bot can do type command /help.";
    private final String AVAILABLE_CURRENCY_NAMES = "Available Crypto Currencies - \n%s";
    private final String HELP_MESSAGE = "This bot updates you about cryptocurrency prices.\n\nYou can use below commands to control price updates.\n\n1. /all - To list all available crypto currencies.\n\n2. /subscribe <currency code from above list> - To subscribe to a currency price updates.\n\n3. /unsubscribe <currency code from above list> - To unsubscribe to a currency price updates.\n\n4. /list - To list all currency subscriptions.";
    private final String UNRECOGNIZED_COMMAND = "Unrecognized command.";
    private final String SUBSCRIBED_MESSAGE = "Hola, you have subscribed to %s price updates for every 5 minute(s).";
    private final String ALREADY_SUBSCRIBED_MESSAGE = "You have already been subscribed to %s price updates.";
    private final String UNSUBSCRIBED_MESSAGE = "You have unsubscribed to %s price updates.";
    private final String NO_SUBSCRIPTION_MESSAGE = "No subscription found for %s price updates.";
    private final String INVALID_MESSAGE = "Invalid input.";
    private final String INVALID_CURRENCY_CHOICE = "Currency does not exist with given input %s. Please enter valid currency.";

    //Repositories
    private ISubscribeCryptoService subscribeCryptoService = BeanUtilService.getBean(SubscribeCryptoService.class);
    private ICryptoCurrencyService cryptoCurrencyService = BeanUtilService.getBean(CryptoCurrencyService.class);

    RedisClient redisClient = new RedisClient();

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage reply = new SendMessage(); // Create a SendMessage object with mandatory fields
            String chatId = update.getMessage().getChatId().toString();
            reply.setChatId(chatId);
            // message.setText(update.getMessage().getText());

            String userId = update.getMessage().getChat().getId().toString();
            String userName = update.getMessage().getChat().getUserName();
            if (userName == null) {
                userName = userId;
            }
            String message = update.getMessage().getText();
            String[] messages = message.split(" ");

            String command = getCommand(message);

            switch (command) {
            case START_COMMAND:
                createReplyMessage(reply, START_MESSAGE);
                break;

            case AVAILABLE_CURRENCY_COMMAND:
                createReplyMessage(reply, String.format(AVAILABLE_CURRENCY_NAMES, getAllCurrencyListString()));
                break;

            case SUBSCRIBE_COMMAND:
                if (messages.length > 1) {
                    String currency = getCurrencyName(messages[1]);
                    if (currency == null) {
                        createReplyMessage(reply, String.format(INVALID_CURRENCY_CHOICE, messages[1]));
                        break;
                    }
                    Boolean subscribed = subscribe(currency, userName, userId, chatId);
                    if (subscribed) {
                        createReplyMessage(reply, String.format(SUBSCRIBED_MESSAGE, currency));
                    } else {
                        createReplyMessage(reply, String.format(ALREADY_SUBSCRIBED_MESSAGE, currency));
                    }
                } else {
                    createReplyMessage(reply, INVALID_MESSAGE);
                }
                break;

            case UNSUBSCRIBE_COMMAND:
                if (messages.length > 1) {
                    String currency = getCurrencyName(messages[1]);
                    if (currency == null) {
                        createReplyMessage(reply, String.format(INVALID_CURRENCY_CHOICE, messages[1]));
                        break;
                    }
                    Boolean unsubscribed = unsubscribe(currency, userName);
                    if (unsubscribed) {
                        createReplyMessage(reply, String.format(UNSUBSCRIBED_MESSAGE, currency));
                    } else {
                        createReplyMessage(reply, String.format(NO_SUBSCRIPTION_MESSAGE, currency));
                    }
                } else {
                    createReplyMessage(reply, INVALID_MESSAGE);
                }
                break;

            case LIST_SUBSCRIPTION_COMMAND:
                List<String> subscriptionList = getAllSubscription(userName);
                var replyMessString = "Subscribed currency list - \n\n";
                if (!(subscriptionList.size() > 0)) {
                    replyMessString = "No subscribed currency. Please subscribe to at least one currency price update and check again.";
                }
                int i = 1;
                for (String string : subscriptionList) {
                    replyMessString += String.format("%s. %s\n", i, StringUtils.capitalize(string.replaceAll("_", " ")));
                    i++;
                }
                createReplyMessage(reply, replyMessString);
                break;

            case HELP_COMMAND:
                createReplyMessage(reply, HELP_MESSAGE);
                break;

            default:
                createReplyMessage(reply, UNRECOGNIZED_COMMAND);
            }
            sendMessage(reply);
        }
    }

    @Override
    public String getBotUsername() {
        return CRYPTO_BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return CRYPTO_BOT_TOKEN;
    }

    private String getCurrencyName(String name) {
        
        name = name.toLowerCase().replaceAll(" ", "_");
        Optional<CryptoCurrency> cc = cryptoCurrencyService.findByName(name);
        if (!cc.isEmpty()) {
            return cc.get().getName();
        }
        return null;
    }

    private SendMessage createReplyMessage(SendMessage replyMessage, String text) {
        replyMessage.setText(text);
        return replyMessage;
    }

    private String getCommand(String message) {
        String[] messages = message.split(" ");
        String command = messages[0].replaceAll("/", "");
        return command;
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private Boolean subscribe(String currency, String user, String userId, String chatId) {
        
        SubscribeCrypto sc = new SubscribeCrypto();
        Optional<SubscribeCrypto> sco =  subscribeCryptoService.findByUserAndCurrency(user, currency);

        if (!sco.isEmpty()) {
            return false;
        }
        sc.setUser(user);
        sc.setActive(true);
        sc.setCryptoCurrency(currency);
        sc.setTelegramChatId(chatId);
        sc.setTelegramUserId(userId);
        subscribeCryptoService.save(sc);

        String subscribers = redisClient.getAllEntriesForCurrency(currency);
        subscribers = subscribers != null ? subscribers + " " : "";
        subscribers = subscribers + sc.getTelegramChatId();
        subscribers = subscribers.trim();
        redisClient.addEntry(currency, subscribers);

        return true;
    }

    private Boolean unsubscribe(String currency, String user) {
        
        Optional<SubscribeCrypto> sco =  subscribeCryptoService.findByUserAndCurrency(user, currency);
        if (!sco.isEmpty()) {
            SubscribeCrypto sc = sco.get();
            sc.setActive(false);
            subscribeCryptoService.save(sc);

            String subscribers = redisClient.getAllEntriesForCurrency(currency);
            subscribers = subscribers != null ? subscribers + " " : "";
            subscribers = subscribers.replaceAll(sc.getTelegramChatId(), "");
            subscribers = subscribers.trim();
            redisClient.addEntry(currency, subscribers);

            return true;
        }
        return false;
    }

    private List<String> getAllSubscription(String userName) {

        List<SubscribeCrypto> scList = subscribeCryptoService.findByUsername(userName);
        List<String> currencyList = new ArrayList<>(); 
        for (SubscribeCrypto subscribeCrypto : scList) {
            currencyList.add(subscribeCrypto.getCryptoCurrency());
        }
        return currencyList;

    }

    private String getAllCurrencyListString() {

        List<CryptoCurrency> ccList = cryptoCurrencyService.findAll();
        String ccMeString = "";
        int i = 1;
        for (CryptoCurrency cryptoCurrency : ccList) {
            ccMeString += String.format("%s. %s - %s\n", i, cryptoCurrency.getDisplayName(), cryptoCurrency.getName());
            i++;
        }
        return ccMeString;
    }

}