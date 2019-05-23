package cn.aysst.www.doctor.utils;

import java.security.MessageDigest;
import java.security.PublicKey;

public class Http {
    public static String TAG = "打印 TAG";
    public static String BASE_URL = "http://172.29.240.142:8080";
//    public static String BASE_URL = "https://www.aysst.cn";
    //public static String BASE_IP = "10.11.12.10";
    public static String BASE_IP = "129.211.11.232";
    public static int BASE_PORT = 11000;
    //public static String FILESTORAGE_ADDR = "E:/HHU/SecondYear";//存储文件路径位置
    public static String FILESTORAGE_ADDR = "/usr/projects/aysst";//存储文件路径位置

    public static String getMD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }



}
