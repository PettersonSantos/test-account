package com.test.payment.account;

import com.test.payment.account.config.FlywayTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {AccountApplication.class, FlywayTestConfig.class})
class AccountApplicationTests {

	@Test
	void contextLoads() {
	}

}
