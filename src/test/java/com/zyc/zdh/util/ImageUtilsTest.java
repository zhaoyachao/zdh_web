package com.zyc.zdh.util;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.*;

class ImageUtilsTest {

    @Test
    void urlToBase64() throws UnsupportedEncodingException {
        String ticket = "";
        String encodedTicket = URLEncoder.encode(ticket, "UTF-8").replaceAll("\\+", "%20");
        String imageUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + encodedTicket;
        String image = ImageUtils.urlToBase64(imageUrl);
        System.out.println(image);
    }
}