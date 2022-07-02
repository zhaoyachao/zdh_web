package com.zyc.zdh.api;

import com.zyc.zdh.ZdhApplication;
import com.zyc.zdh.monitor.Sys;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ZdhApplication.class})
@ActiveProfiles(profiles = {"dev"})
public class PermissionApiTest {

    public String product_code="zdh";
    public String ak="c5f503f09d99b03ea436408ea979ada8";
    public String sk="95a064009b8b729472952378cfc861fc";

    @Autowired
    PermissionApi permissionApi;
    @Test
    public void get_user_list_by_product_role() {

        String role_code="admin";
        String result = permissionApi.get_user_list_by_product_role(product_code,role_code ,ak, sk);

        System.out.println(result);
    }
}