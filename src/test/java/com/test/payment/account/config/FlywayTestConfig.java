package com.test.payment.account.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;

@TestConfiguration
public class FlywayTestConfig {

    @Bean
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", "sa", "password")
                .locations("classpath:db/test/migration")
                .load();
    }
}
