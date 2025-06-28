package com.telegrambot.Repository;

import com.telegrambot.Entity.TelegramRecordEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramRepository extends MongoRepository<TelegramRecordEntity, String> {



}
