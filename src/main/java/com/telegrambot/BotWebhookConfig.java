package com.telegrambot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetWebhook;

@Configuration
public class BotWebhookConfig {

    @Bean
    public CommandLineRunner registerWebhook(@Value("${telegram.bot.token}") String token,
                                            @Value("${telegram.webhook.url}") String webhookUrl) {
                                                System.out.println("BotWebhookConfig.registerWebhook()");
        return args -> {
            TelegramBot bot = new TelegramBot(token);
            bot.execute(new SetWebhook().url(webhookUrl + "/update"));
        };
    }
}
