package com.zyc.zdh.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.entity.ProcessFlowInfo;
import com.zyc.zdh.util.HttpUtil;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

public class TestA {

    @Test
    public void run() throws Exception {

        JSONObject jo = new JSONObject();
        jo.put("a", "1");
        jo.put("a", "2");

        System.out.println(jo.toJSONString());


    }

    @Test
    public void run2() throws Exception {

        int[] s1 = new int[]{1, 3, 5, 2, 4, 6, 9, 100, 30, 0};

        sort(s1, 0, s1.length - 1);


        System.out.println(Arrays.toString(s1));


    }

    public void sort(int[] s1, int start, int end) {
//临界数据

        int x = start;
        int y = end;
        if (x > y) {
            return;
        }
        int tmp = s1[start];
//遍历start ,end 数据 大于tmp 的放右边,小于tmp 的放左边
        while (x < y) {
            while (s1[y] > tmp && y > x) {
                y--;
            }
            while (s1[x] <= tmp && x < y) {
                x++;
            }
            if (x < y) {
                int z = s1[y];
                s1[y] = s1[x];
                s1[x] = z;
            }

        }
        s1[start] = s1[y];
        s1[y] = tmp;
        sort(s1, start, y - 1);
        sort(s1, y + 1, end);
    }


    @Test
    public void run3() throws Exception {

        int[] s1 = new int[]{1, 3, 5, 7, 9};

        int[] s2 = new int[]{0, 2, 4, 6, 8, 10, 20, 40};
        sort2(s1, s2);


        //System.out.println(Arrays.toString(s1));


    }

    public void sort2(int[] s1, int[] s2) {

        int x = 0;
        int y = 0;
        int[] s3 = new int[s1.length + s2.length];
        int i = 0;
        while (i < s3.length) {
            while (x < s1.length && y < s2.length) {
                if (s1[x] < s2[y]) {
                    s3[i] = s1[x];
                    x++;
                } else {
                    s3[i] = s2[y];
                    y++;
                }
                i++;
            }

            System.out.println("x:" + x);
            //s1 已经完了,s2未完
            while (x >= s1.length && y < s2.length) {
                s3[i] = s2[y];
                y++;
                i++;
            }

            while (y >= s2.length && x < s1.length) {
                s3[i] = s1[x];
                x++;
                i++;
            }
        }


        System.out.println(Arrays.toString(s3));


    }

    @Test
    public void testAPI() throws Exception {
        ProcessFlowInfo pfi=new ProcessFlowInfo();
        pfi.setFlow_id("123");
        String result = HttpUtil.postJSON("http://127.0.0.1:8081/api/call_back_test", JSON.toJSONString(pfi));
        System.out.println(result);
    }

}
