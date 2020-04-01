package com.learn.concurrent.classloader.chapter3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 我们自己定的的ClassLoader, 我们打算从文件中去拿取
 * 也就是去磁盘定义一个目录
 * @ClassName: MyClassLoader
 * @Description:
 * @Author: lin
 * @Date: 2020/4/1 15:20
 * History:
 * @<version> 1.0
 */
public class MyClassLoader extends  ClassLoader{
    /**
     * 磁盘定义一个目录
     */
    private static  final String DEFAULT_DIR = "D:\\app\\classloader";

    /**
     * 定义一个dir, 让别人可以传入,如果不传入那么就是默认值
     */
    private String dir = DEFAULT_DIR;

    /**
     * classLoader的名字
     */
    private String classLoaderName;

    /**
     * 默认构造函数
     */
    MyClassLoader(){
        super();
    }

    /**
     * 这个设置 classLoader的名字
     * @param classLoaderName
     */
    MyClassLoader(String classLoaderName){
        super();
        this.classLoaderName = classLoaderName;
    }

    /**
     * 将去 父类加载器传入进去, 如果不传入那么就是 系统类加载默认值
     * @param classLoaderName
     * @param parent
     */
    public  MyClassLoader(String classLoaderName, ClassLoader parent){
        super(parent);
        this.classLoaderName = classLoaderName;
    }

    /**
     * 重写这个 方法，通过这个方法去查找父类加载器
     * 这个 name传入 的是 xxx.xxx.xxx.AA 等
     * 就是去读取文件, 然后将其转换为符合加载的格式
     * xxx/xxx/xxx/xxx/AAA.class
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classPath = name.replace(".", "/");
        // 文件是 classPath 然后加上 .class 才是class文件
        // dir是父目录  D:\\app\\classloader + com/learn/concurrent/classloader/chapter3/MyObject.class;
        File classFile = new File(dir , classPath + ".class");
        // D:\app\classloader\com\learn\concurrent\classloader\chapter3\MyObject.class
        if(!classFile.exists()){
            throw  new ClassNotFoundException("The class " + name + " not fount under");
        }
        //将文件读成一个字节数组
        byte[] classBytes = loadClassBytes(classFile);
        if(null == classBytes && classBytes.length == 0){
            throw  new ClassNotFoundException("load the class " + name + " failed");
        }

        //返回 这定义的类 ，从0开始读取， 可以读取多个class, 从0~1000 是A class
        // 从1000~200是B class
        return this.defineClass(name, classBytes, 0, classBytes.length);
    }

    /**
     * 将一个文件变成一个数组
     * @param classFile
     * @return
     */
    private byte[] loadClassBytes(File classFile) {
        //写成这样的好处就是 不需要你自己去释放流，它会自己去释放
        // 把文件输入流，转换成内存输出流
        try(ByteArrayOutputStream b = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(classFile)) {
           // 用byte来缓冲 ,一次最多读多少个
            byte[] buffer = new byte[1024];
            // 读了多少个 ，如果是负1那么就没有了
            int len ;
            // 这个将文件中数据读到 byte数组中
            while ((len = fis.read(buffer))!=-1){
                // byte数组 写，这个将buffer的写到 byte数组中
                  b.write(buffer, 0, len);
            }
            //写完之后 flush
            b.flush();
            return  b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }

    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getClassLoaderName() {
        return classLoaderName;
    }
}
