package com.a6raywa1cher.websecurityspringbootstarter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebSecuritySpringBootStarterApplicationTests {
//	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
//		.withUserConfiguration(TestAppConfiguration.class)
//		.withPropertyValues(
//			"web-security.jwt.secret", "meow",
//			"web-security.jwt.refresh-duration", "P1D",
//			"web-security.jwt.access-duration", "PT5M",
//			"web-security.jwt.issuer-name", "test-app",
//			""
//		);

	@Test
	void contextLoads() {

	}

}
