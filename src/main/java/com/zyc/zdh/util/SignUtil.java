package com.zyc.zdh.util;

import com.google.common.collect.Lists;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SignUtil {
    /**
     * 统一生成签名工具类
     * 1 只加密基础类型和String
     * @param param
     * @param signKey
     * @return
     */
    public static String generatSign(Map<String, Object> param, String signKey){
        try{
            List<String> keys = Lists.newArrayList(param.keySet());
            keys.sort(Comparator.reverseOrder());
            StringBuilder sb = new StringBuilder();
            for (String key: keys){
                if(key.equalsIgnoreCase("sign")){
                    continue;
                }
                if(Objects.isNull(param.get(key))){
                    continue;
                }

                //只加密基础类型
                if(!isWrapperClass(param.get(key).getClass())){
                    continue;
                }
                sb.append("&").append(key).append("=").append(param.get(key));
            }
            sb.append(signKey);
            String signStr = sb.toString().substring(1);

            MessageDigest md = MessageDigest.getInstance("MD5");
            // 更新数据
            byte[] messageDigest = md.digest(signStr.getBytes());
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static boolean isWrapperClass(Class<?> clazz) {
        return clazz != null && (
                clazz == Byte.class ||
                        clazz == Short.class ||
                        clazz == Integer.class ||
                        clazz == Long.class ||
                        clazz == Float.class ||
                        clazz == Double.class ||
                        clazz == Character.class ||
                        clazz == Boolean.class ||
                        clazz == String.class
        );
    }
}
