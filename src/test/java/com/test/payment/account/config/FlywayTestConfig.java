package com.test.payment.account.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;

@TestConfiguration
public class FlywayTestConfig {

    @Autowired
    private Environment env;

    @Bean
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(env.getProperty("spring.flyway.url"), env.getProperty("spring.flyway.user"), env.getProperty("spring.flyway.password"))
                .locations(env.getProperty("spring.flyway.locations"))
                .load();
    }

    @PostConstruct
    public void migrateFlyway() {
        flyway().migrate();
    }
}
