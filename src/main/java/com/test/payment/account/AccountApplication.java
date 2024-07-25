package com.test.payment.account;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.output.MigrateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.test.payment.account.domain")
public class AccountApplication {

	private static final Logger logger = LoggerFactory.getLogger(AccountApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

	@Bean
	public FlywayMigrationStrategy flywayMigrationStrategy() {
		return flyway -> {
			FluentConfiguration configuration = Flyway.configure()
					.dataSource(flyway.getConfiguration().getDataSource())
					.locations("classpath:db/migration");

			Flyway newFlyway = new Flyway(configuration);
			MigrateResult result = newFlyway.migrate();
			System.out.println("Flyway migration result: " + result.success);
		};
	}

	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {
		ConfigurableEnvironment env = (ConfigurableEnvironment) event.getApplicationContext().getEnvironment();
		logger.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));
		logger.info("Datasource URL: {}", env.getProperty("spring.datasource.url"));
	}

}
