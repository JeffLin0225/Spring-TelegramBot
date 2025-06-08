package com.telegrambot.Service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;

import jakarta.annotation.PostConstruct;

@Service
public class BotService {

    private  TelegramBot bot;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.mychatid}")
    private long myChatId;

    @PostConstruct
    public void init() {
        bot = new TelegramBot(botToken); // 使用設定檔中的 Token

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    String messageText = update.message().text();
                    long chatId = update.message().chat().id();
                    System.out.println("接收到 chatId: " + chatId);
                    System.out.println("接收到 訊息: " + messageText);

                    // 回傳同樣的文字
                    // bot.execute(new SendMessage(chatId, "你說了：" + messageText));
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void sendMessageToMe(String msg){
        bot.execute(new SendMessage(myChatId, msg));
    }

    public void sendImgToMe(String msg){
        File file = new File("/Users/linjiaxian/Desktop/Testdata/im2.jpeg");
        SendPhoto img = new SendPhoto(myChatId, file).caption(msg);
        bot.execute(img);
    }
}