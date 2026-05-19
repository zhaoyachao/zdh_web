package com.zyc.zdh.util;

import org.springframework.util.SerializationUtils;

import java.io.*;
import java.util.Base64;

public class SerializationUtil {

    // deserialize to Object from given file
    public static Object deserialize(String fileName) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

    // serialize the given object and save it to file
    public static void serialize(Object obj, String fileName)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);

        fos.close();
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        System.out.println(new String(SerializationUtils.serialize("zdh_web_1:903828503655354368")));

        byte[] bytes = "\\xAC\\xED\\x00\\x05t\\x00\\x1Czdh_web_1:903828503655354368".getBytes();
        //Base64 Encoded
        String encoded = Base64.getEncoder().encodeToString(bytes);

        //Base64 Decoded
        byte[] decoded = Base64.getDecoder().decode(encoded);
        System.out.println(SerializationUtils.deserialize(decoded));

        ///SerializationUtil.deserialize("\\xAC\\xED\\x00\\x05t\\x00\\x1Czdh_web_1:903812864916066304");
    }

}