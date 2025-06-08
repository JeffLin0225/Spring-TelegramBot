package com.telegrambot.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.telegrambot.Service.BotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class BotController {
    private final BotService botService;

    public BotController(BotService botService){
        this.botService = botService;
    }

    @GetMapping("/chat")
    public void chat(@RequestParam String msg) {
        botService.sendMessageToMe(msg);
    }

    @GetMapping("/chatwebhook")
    public void chatwebhook(@RequestParam String msg) {
        botService.sendImgToMe(msg);
    }
    
}
