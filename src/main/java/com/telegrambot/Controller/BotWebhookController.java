package com.telegrambot.Controller;

import com.telegrambot.Service.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger log = LoggerFactory.getLogger(BotWebhookController.class);
    private final TelegramBot bot;

    @Autowired
    public TelegramService telegramService;

    public BotWebhookController( @Value("${telegram.bot.token}")String botToken ,TelegramService telegramService ){
        this.bot = new TelegramBot(botToken);
        this.telegramService = new TelegramService();
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
    public void test() {
        try {
            bot.execute(new SendMessage(myChatId, "測試webhook有通！"));
            log.warn("TEST Webhook PASS！");
        } catch (Exception e) {
            log.error("TEST Webhook ERROR : "+e.toString());
        }
    }

    @GetMapping("/sendMessage")
    public void sendMessage(@RequestParam String msg){
        try {
            telegramService.insertRecord(msg);
        }catch ( Exception e  ){
            log.error("(ERROR) Insert Record -> MongoDB : "+e);
        }finally {
            bot.execute(new SendMessage(myChatId , msg ));
            log.warn("(Success) Send -> Telegram : " + msg);
        }
    }
}
