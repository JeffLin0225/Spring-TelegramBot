package com.telegrambot.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

@RestController
public class BotWebhookController {
    private final TelegramBot bot;

    public BotWebhookController( @Value("${telegram.bot.token}")String botToken){
        this.bot = new TelegramBot(botToken);
    }

        @Value("${telegram.bot.mychatid}")
        private long myChatId;

    @PostMapping("/update")
    public void onUpdate(@RequestBody Update update){
        if(update.message() != null && update.message().text() != null){
            long chatId = update.message().chat().id();
            String message = update.message().text();

            System.out.println("Webhook 收到ChatId = "+ chatId);
            System.out.println("Webhook 收到訊息 = "+message);
            bot.execute(new SendMessage(chatId,"從server端傳來你說的："+ message));
        }
    }
    @GetMapping("/test")
    public void test(@RequestParam String msg) {
        bot.execute(new SendMessage(myChatId, "測試webhook有通！"));
    }

    @GetMapping("/chatwebhook")
    public void chatwebhook(@RequestParam String msg) {
        bot.execute(new SendMessage(myChatId, msg));
    }
}
