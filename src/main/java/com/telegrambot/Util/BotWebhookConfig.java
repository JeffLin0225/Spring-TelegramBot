package com.telegrambot;


import com.telegrambot.Controller.BotWebhookController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetWebhook;

@Configuration
public class BotWebhookConfig {
    private static final Logger log = LoggerFactory.getLogger(BotWebhookConfig.class);

    @Bean
    public CommandLineRunner registerWebhook(@Value("${telegram.bot.token}") String token,
                                            @Value("${telegram.webhook.url}") String webhookUrl) {
        try {
            log.warn("BotWebhookConfig.registerWebhook()");
            return args -> {
                TelegramBot bot = new TelegramBot(token);
                bot.execute(new SetWebhook().url(webhookUrl + "/update"));
            };
        } catch (Exception e) {
            log.error("BotWebhookConfig.registerWebhook() error : "+e.toString());
            return null;
        }

    }
}
