package com.zyc.zspringboot.kafka;

import com.zyc.zspringboot.ProfilesResolver;
import com.zyc.zspringboot.ZspringbootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zyc-admin
 * @data 2018-03-29 11:26
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ZspringbootApplication.class})
@ActiveProfiles(resolver = ProfilesResolver.class)
public class KafkaTest {

	@Autowired
	private MyKafkaConsumer myKafkaConsumer;

	@Test
	public void send() {
		for (int i = 0; i < 10; i++) {
			myKafkaConsumer.send("data" + i);
		}
		while (true) {

		}
	}
}
