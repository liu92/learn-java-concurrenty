package com.learn.concurrent.classloader.chapter4;

import com.learn.concurrent.classloader.chapter4.EncryptUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @ClassName: DecryptClassLoader
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 22:25
 * History:
 * @<version> 1.0
 */
public class DecryptClassLoader extends ClassLoader {
    private static  final String DEFAULT_DIR = "D:\\app\\classloader3";
    private String dir = DEFAULT_DIR;

    public DecryptClassLoader(){
        super();
    }

    public DecryptClassLoader(ClassLoader parent){
        super(parent);
    }

    @Override
    protected Class<?> findClass(String name)
            throws ClassNotFoundException {
        String classPath = name.replace(".", "/");
        File classFile = new File(dir, classPath + ".class");
        if(!classFile.exists()){
            throw  new ClassNotFoundException("The class " + name + " not fount under directory ["+dir+"]");
        }

        //将文件读成一个字节数组
        byte[] classBytes = loadClassBytes(classFile);
        if(null == classBytes || classBytes.length == 0){
            throw  new ClassNotFoundException("load the class " + name + " failed ");
        }
        return super.defineClass(name, classBytes, 0, classBytes.length);
    }

    /**
     * 将一个文件变成一个数组
     * @param classFile
     * @return
     */
    private byte[] loadClassBytes(File classFile) {
        try(ByteArrayOutputStream bas = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(classFile)) {

            int len ;
            // 这个将文件中数据读到 byte数组中
            while ((len = fis.read())!=-1){
                bas.write(len ^ EncryptUtils.ENCRYPT_FACTOR);
            }
            //写完之后 flush
            bas.flush();
            return  bas.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }

    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
