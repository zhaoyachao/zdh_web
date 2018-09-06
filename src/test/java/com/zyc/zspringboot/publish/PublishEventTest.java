package com.zyc.zspringboot.publish;

import com.zyc.zspringboot.ZspringbootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zyc.zspringboot.publish.MyPublishEvent;
import com.zyc.zspringboot.util.SpringContext;

/**
 * ClassName: PublishEventTest   
 * @author zyc-admin
 * @date 2018年2月7日  
 * @Description: TODO  
 */
@SpringBootTest(classes={ZspringbootApplication.class})
@RunWith(SpringRunner.class)
public class PublishEventTest {
	
	@Test
	public void publish(){
		SpringContext.publishEvent(new MyPublishEvent(new String("hello")));
		
	}
}
