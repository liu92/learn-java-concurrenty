package com.learn.concurrenty.chapter3;

/**
 * @ClassName: CreateThread4
 * @Description:
 * @Author: lin
 * @Date: 2020/3/20 15:08
 * History:
 * @<version> 1.0
 */
public class CreateThread4 {

    private static  int counter =1;

    public static void main(String[] args) {
//       Thread t1 = new Thread(new Runnable() {
//           @Override
//           public void run() {
//               try {
//                   add(1);
//               }catch (Error e){
//                   System.out.println(counter);
//               }
//           }
//           private  void add(int i){
//               ++counter;
//               add(i + 1);
//           }
//       });
//       t1.start();
        //可以去修改stack大小
        Thread t1 = new Thread(null,new Runnable() {
            @Override
            public void run() {
                try {
                    add(1);
                }catch (Error e){
                    System.out.println(counter);
                }
            }
            private  void add(int i){
                ++counter;
                add(i + 1);
            }
        },"test", 1<<24);
        t1.start();

    }


}
