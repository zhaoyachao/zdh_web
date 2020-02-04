package com.zyc.zspringboot;

import org.apache.shiro.crypto.hash.SimpleHash;

public class T1 {

    public static void main(String[] args) {

        Object obj =   new SimpleHash("md5", new String("123456"), null, 1);

        System.out.println(obj.toString());

        String str2="jdbc:mysql://localhost:3306/test";
        String str="jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=true";

        int index=str.split("\\?")[0].lastIndexOf("/");

        System.out.println(str.split("\\?")[0].substring(index+1));

        int index2=str2.split("\\?")[0].lastIndexOf("/");

        System.out.println(str2.split("\\?")[0].substring(index2+1));


        str.substring(index);

    }
}
