package com.zyc.zdh.service.impl;

import com.zyc.zdh.ZdhApplication;
import com.zyc.zdh.service.JemailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest(classes= ZdhApplication.class)
@RunWith(SpringRunner.class)
public class JemailServiceImplTest {

    @Autowired
    JemailService jemailService;
    @Test
    public void sendEmail() throws Exception {

        jemailService.sendEmail("1299898281@qq.com","zhuti","nihao");
    }

}