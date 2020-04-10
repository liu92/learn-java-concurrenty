package com.learn.concurrenty.juc.untils.countdownlatch;

import com.learn.concurrenty.DefaultThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @ClassName: CountDownLatchExample1
 * @Description:
 * @Author: lin
 * @Date: 2020/4/8 16:57
 * History:
 * @<version> 1.0
 */
public class CountDownLatchExample4 {
   private static Random random = new Random(System.currentTimeMillis());


   private static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(5,
            20, 200,
            TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(20),
            new DefaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    private static ExecutorService EXECUTOR_SERVICE_FIXED = Executors.newFixedThreadPool(5);

    public static void main(String[] args)  {
         Event[] events = {new Event(1), new Event(2)};
        for (Event event : events) {
            List<Table> tables = capture(event);
            TaskGroup taskGroup = new TaskGroup(tables.size(), event);
            for (Table table : tables) {
                TaskBatch taskBatch = new TaskBatch(taskGroup,2);
                TrustSourceColumns columnsRunnable = new TrustSourceColumns(table, taskBatch);
                TrustSourceRecordCount recordCountRunnable = new TrustSourceRecordCount(table,
                        taskBatch);
                // 每个table 执行结束后,而且所有的记录都在 sourceColumnSchema里面去做了
                // 并且update 只是update了一次
                EXECUTOR_SERVICE.execute(columnsRunnable);
                EXECUTOR_SERVICE.execute(recordCountRunnable);
            }
        }
    }


    static  class Event {
        int id = 0;
        Event(int id){
            this.id = id;
        }

    }

    interface Watcher{
        /**
         * 开始监听
         */
//        void startWatch();

        /**
         * 已经完成
         * @param table
         */
        void done(Table table);
    }

    static class TaskBatch implements Watcher{

        private  CountDownLatch countDownLatch;
        //要知道是那个event ,那么 这里加上 TaskGroup
        private TaskGroup taskGroup;

        public  TaskBatch(TaskGroup taskGroup, int size){
           this.taskGroup = taskGroup;
           this.countDownLatch = new CountDownLatch(size);
        }

//        @Override
//        public void startWatch() {
//
//        }

        @Override
        public void done(Table table) {
          //如果完成一次 计算器就减一次
          countDownLatch.countDown();
          if(countDownLatch.getCount() == 0){
              System.out.println("The table " + table.tableName + " finished work, " +
                      "[" + table+"]");
              //当event做完了,那么就是调用下 group来 看看是那个event
              taskGroup.done(table);
          }
        }
    }


    static class TaskGroup implements Watcher{

        private  CountDownLatch countDownLatch;

        private Event event;

        public  TaskGroup(int size, Event e){
            this.event = e;
            this.countDownLatch = new CountDownLatch(size);
        }

        @Override
        public void done(Table table) {
            countDownLatch.countDown();
            if(countDownLatch.getCount() == 0){
                System.out.println("===========All of  table done in event: " + event.id );
            }
        }
    }


    static  class Table {
        String tableName;
        long sourceRecordCount = 10;
        long targetCount;
        String sourceColumnSchema = "<table name='a'><column name='col1' type='varchar2'/></table>";
        String targetColumnSchema = "";

       public Table(String tableName, long sourceRecordCount){
            this.tableName = tableName;
            this.sourceRecordCount = sourceRecordCount;
        }

        @Override
        public String toString() {
            return "Table{" +
                    "tableName='" + tableName + '\'' +
                    ", sourceRecordCount=" + sourceRecordCount +
                    ", targetCount=" + targetCount +
                    ", sourceColumnSchema='" + sourceColumnSchema + '\'' +
                    ", targetColumnSchema='" + targetColumnSchema + '\'' +
                    '}';
        }
    }

     private static List<Table> capture(Event event){
        List<Table> list = new ArrayList<>();
        int count = 10;
         for (int i = 0; i <count ; i++) {
             list.add(new Table("table-"+event.id+"-" + i, i*1000));
         }
         return list;
     }


    static class  TrustSourceRecordCount implements Runnable{
        private final  Table table;
        private final TaskBatch taskBatch;

        public TrustSourceRecordCount(Table table,TaskBatch taskBatch) {
            this.table = table;
            this.taskBatch = taskBatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            table.targetCount = table.sourceRecordCount;
            //当任务执行完,那么就调用 TaskBatch中done方法
            // 然后就减一下 计数器, 每一个都要减,
            // 那么就将这个方法放到 TaskBatch中去 进行封装
            taskBatch.done(table);
//            System.out.println("The table " + table.tableName +
//                    " target record count capture done and update the data.");
        }
    }


     static class  TrustSourceColumns implements Runnable{
        private final  Table table;
        private final TaskBatch taskBatch;

         public TrustSourceColumns(Table table, TaskBatch taskBatch) {
             this.table = table;
             this.taskBatch = taskBatch;
         }

         @Override
         public void run() {
             try {
                 Thread.sleep(random.nextInt(10000));
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             table.targetColumnSchema = table.sourceColumnSchema;
             //当任务执行完,那么就调用 TaskBatch中done方法
             // 然后就减一下 计数器, 每一个都要减,
             // 那么就将这个方法放到 TaskBatch中去 进行封装
             taskBatch.done(table);
//             System.out.println("The table " + table.tableName +
//                     " target columns capture done and update the data.");
         }
     }
}
