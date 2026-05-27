package com.chatapp.bff;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "SECURITY_JWT_SECRET=mysupersecretkeymysupersecretkey123456"
        }
)
class BffApplicationTests {

	@Test
	void contextLoads() {
	}

}
