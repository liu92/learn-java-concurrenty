package com.learn.concurrent.classloader.chapter4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ClassName: EncryptUtils
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 22:21
 * History:
 * @<version> 1.0
 */
public class EncryptUtils {
    public static  final byte ENCRYPT_FACTOR = (byte) 0xff;

    private EncryptUtils(){

    }

    public static void decrypt(String source, String target){
        try(FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(target)) {
           int data ;
           while ((data = fis.read()) !=-1){
               fos.write(data^ENCRYPT_FACTOR);
           }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        decrypt("D:\\app\\classloader\\a.txt","D:\\app\\classloader\\b.txt");
    }

}
