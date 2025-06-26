package com.telegrambot.Controller;

import com.telegrambot.Entity.TestEntity;
import com.telegrambot.Repository.TestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private TestRepository testRepository;

    @GetMapping("/findUser")
    public TestEntity findUser(@RequestParam String username){
        TestEntity userList = testRepository.findByUserName(username);
        if(userList == null){
            System.out.println("空的");
            return null;
        }
        return userList;
    }

    @GetMapping("/findALL")
    public List<TestEntity> findALL(){
        List<TestEntity> userList = testRepository.findAll();
        if(userList == null){
            return null;
        }
        return userList;
    }

    @GetMapping("/createUser")
    public void createUser(@RequestParam String username , @RequestParam int age){
        try {
            TestEntity testEntity = new TestEntity();
            testEntity.setName(username);
            testEntity.setAge(age);
            testRepository.save(testEntity);
            log.warn("Create User Success!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
