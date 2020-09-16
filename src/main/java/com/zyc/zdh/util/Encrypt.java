package com.zyc.zdh.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//引入第三方包
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;


public class Encrypt {

    //--------------AES---------------
    private static final String KEY = "zdhmyabcde123456";  // 密匙，必须16位
    private static final String OFFSET = "5e8y6w45ju8w9jq8"; // 偏移量
    private static final String ENCODING = "UTF-8"; // 编码
    private static final String ALGORITHM = "AES"; //算法
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"; // 默认的加密算法，CBC模式


    /**
     *  AES加密
     * @param data
     * @return String
     * @author anson
     * @date   2019-8-24 18:43:07
     */
    public static String AESencrypt(String data) throws Exception
    {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("ASCII"), ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(OFFSET.getBytes());//CBC模式偏移量IV
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(data.getBytes(ENCODING));
        return new Base64().encodeToString(encrypted);//加密后再使用BASE64做转码
    }

    /**
     * AES解密
     * @param data
     * @return String
     * @author anson
     * @date   2019-8-24 18:46:07
     */
    public static String AESdecrypt(String data) throws Exception
    {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("ASCII"), ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(OFFSET.getBytes()); //CBC模式偏移量IV
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] buffer = new Base64().decode(data);//先用base64解码
        byte[] encrypted = cipher.doFinal(buffer);
        return new String(encrypted, ENCODING);
    }

    public static void main(String[] args) throws Exception {

        String my=AESencrypt("123456");

        String passwd=AESdecrypt(my);

        System.out.println(my);
        System.out.println(passwd);
    }
}