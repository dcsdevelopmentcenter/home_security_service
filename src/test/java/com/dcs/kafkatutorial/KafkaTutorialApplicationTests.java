package com.dcs.kafkatutorial;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class KafkaTutorialApplicationTests {

	@Test
	void contextLoads() {
		Map<String, String> stringStringMap = new HashMap<>();
		stringStringMap.put("1","1");
		stringStringMap.put("2","1");

	}

}
