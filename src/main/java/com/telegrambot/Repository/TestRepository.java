package com.telegrambot.Repository;

import com.telegrambot.Entity.TestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends MongoRepository<TestEntity,String> {

    @Query("{name:?0}")
    TestEntity findByUserName(String userName);


}
