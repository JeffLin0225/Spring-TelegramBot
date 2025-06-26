package com.telegrambot.Util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jx.decoder.util.Decryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MongoDBConfig.class);

    @Autowired
    private Decryptor decryptor;

    // 假設 host, database, username, password 都需要加密
    @Value("${mongodb.encrypted.host}")
    private String host;

    @Value("${mongodb.encrypted.database}")
    private String database;

    @Value("${mongodb.encrypted.username}")
    private String encryptedUsername;

    @Value("${mongodb.encrypted.password}")
    private String encryptedPassword;

    // 這個欄位用來儲存解密後的 database 名稱，給 getDatabaseName() 使用
    private String decryptedDatabaseName;

    @Override
    @Bean
    public MongoClient mongoClient() {
        try {
            log.info("正在解密 MongoDB 所有連線參數...");
            // 1. ***【修正】*** 解密所有需要的參數
            String user = decryptor.decrypt(encryptedUsername);
            String password = decryptor.decrypt(encryptedPassword);
            log.info("所有參數解密完成。");

            // 2. ***【修正】*** 將解密後的 database 名稱存起來
            this.decryptedDatabaseName = database;

            // 3. 對帳號密碼進行 URL 編碼
            String encodedUser = URLEncoder.encode(user, StandardCharsets.UTF_8);
            String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);

            // 4. 使用解密和編碼後的乾淨變數來組合 URI
            String connectionUri = String.format(
                    "mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority&compressors=zstd,snappy,zlib",
                    encodedUser,
                    encodedPassword,
                    host,       // <- 使用解密後的 host
                    database    // <- 使用解密後的 database
            );
            log.info("已產生 MongoDB 連線 URI。");

            ConnectionString connectionString = new ConnectionString(connectionUri);

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            log.info("MongoDB 連線客戶端建立成功！準備連線...");
            return MongoClients.create(settings);
        } catch (Exception e) {
            log.error("建立 MongoDB 連線時發生嚴重錯誤！", e);

            throw new IllegalStateException("無法建立資料庫連線，請檢查加密參數與連線字串是否正確！", e);
        }
    }

    @Override
    protected String getDatabaseName() {
        // 5. ***【修正】*** 回傳解密後的資料庫名稱
        return this.decryptedDatabaseName;
    }
}