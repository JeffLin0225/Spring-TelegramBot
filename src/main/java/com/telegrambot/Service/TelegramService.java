package com.telegrambot.Service;

import com.telegrambot.Entity.TelegramRecordEntity;
import com.telegrambot.Repository.TelegramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramService {

    @Autowired
    public TelegramRepository telegramRepo;

    public void insertRecord(String message){
        TelegramRecordEntity record = new TelegramRecordEntity();
        record.setContext(message);
        telegramRepo.save(record);
    }

}
