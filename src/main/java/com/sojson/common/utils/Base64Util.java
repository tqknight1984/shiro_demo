package com.sojson.common.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class Base64Util {
    /**
     * 使用jdk的base64 加密字符串
     * */
    public static String jdkBase64Encoder(String str)
    {
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(str.getBytes());
        return encode;
    }
    /**
     * 使用jdk的base64 解密字符串
     * 返回为null表示解密失败
     * */
    public static String jdkBase64Decoder(String str)
    {
        BASE64Decoder decoder = new BASE64Decoder();
        String decode=null;
        try {
            decode = new String( decoder.decodeBuffer(str));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return decode;
    }

}
