package com.learn.concurrent.classloader.chapter4;


/**
 * @ClassName: SimpleEncrypt
 * @Description: 简单加密
 * @Author: lin
 * @Date: 2020/4/1 22:08
 * History:
 * @<version> 1.0
 */
public class SimpleEncrypt {
   private static  final  String PLAIN = "Hello ClassLoader";

   private static  final byte ENCRYPT_FACTOR = (byte) 0xff;

    public static void main(String[] args) {
       byte[] bytes = PLAIN.getBytes();
       byte[] encrypt = new byte[bytes.length];
        for (int i = 0; i < bytes.length ; i++) {
            //通过位运算来进行简单加密
             encrypt[i] = (byte) ( bytes[i] ^ ENCRYPT_FACTOR);
        }
        System.out.println(new String(encrypt));

        byte[] decrypt = new byte[encrypt.length];
        for (int i = 0; i <encrypt.length ; i++) {
             decrypt[i] =(byte) (encrypt[i] ^ ENCRYPT_FACTOR);
        }
        System.out.println(new String(decrypt));








    }
}
