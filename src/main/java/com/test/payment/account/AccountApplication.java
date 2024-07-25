package com.test.payment.account;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.test.payment.account.domain")
public class AccountApplication {

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

}
