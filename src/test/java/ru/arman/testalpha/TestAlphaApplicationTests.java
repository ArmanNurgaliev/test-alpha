package ru.arman.testalpha;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"SELECTED_REPO=JPA",
		"SERVER_PORT=8080"
})
class TestAlphaApplicationTests {


	@Test
	void contextLoads() {
		System.out.println("Context load!!!");
	}

}
