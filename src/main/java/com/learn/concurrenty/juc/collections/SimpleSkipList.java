package com.learn.concurrenty.juc.collections;

import java.util.Random;

/**
 * @ClassName: SimpleSkipList
 * @Description: 简单跳表
 * @Author: lin
 * @Date: 2020/4/16 14:46
 * History:
 * @<version> 1.0
 */
public class SimpleSkipList {
    /**
     * 跳表的头和尾 都对应下层链表的头和尾
     */
    private final static byte HEAD_NODE = (byte) -1;
    private final static byte DATA_NODE = (byte) 0;
    private final static byte TAIL_NODE = (byte) -1;

    private static class Node{
        private Integer value;
        private Node up;
        private Node down;
        private Node left;
        private Node right;
        private byte bit;

        public  Node(Integer value, byte bit){
            this.value = value;
            this.bit = bit;
        }

        public Node(Integer value){
            this(value, DATA_NODE);
        }
    }

    /**
     * 节点的头
     */
    private Node head;
    /**
     * 节点的尾
     */
    private Node tail;

    /**
     * 节点的元素个数
     */
    private int size;
    /**
     * 高度
     */
    private int height;
    /**
     * 简单的随机算法
     */
    private Random random;

    /**
     * 构造方法 这个跳表中 头节点，和尾节点
     *
     */
    public  SimpleSkipList(){
        this.head = new Node(null, HEAD_NODE);
        this.tail = new Node(null , TAIL_NODE);
        head.right = tail;
        tail.left = head;
        // 简单随机数
        this.random = new Random(System.currentTimeMillis());
    }


    /**
     *  最底层 包含所有元素
     * 分层查找
     * @param element
     * @return
     */
    private Node find(Integer element){
        Node current = head;
        for(;;){
            // 不是最后一个 并且
            while (current.right.bit !=TAIL_NODE && current.right.value <= element){
                current = current.right;
            }
            // 如果下面的指针 不为空, 那么就将当前节点的下面指针
            // 指向 当前
            if(current.down != null){
                current = current.down;
            }else {
                break;
            }
        }
        // the current<=element< current.right(if exist)
        return  current;
    }

    /**
     * 插入元素
     * @param element
     */
    public void  add(Integer element){
        // 首先查询这个元素 相近的元素
        Node findNode = this.find(element);
        // 声明一个node
        Node newNode = new Node(element);

        /**  双向
         *
         *   +------+      +-----+        +-----+
         *            --->           --->
         *    9(node)      newNode         20(node)
         *            <---           <---
         *   +------+      +-----+        +-----+
         */

        newNode.left = findNode;
        // 那么新节点的 右指针 是 临近节点的右指针
        newNode.right = findNode.right;
        // 临近节点的 右指针的 左指针 就是 新节点
        findNode.right.left = newNode;
        findNode.right = newNode;


        // 需不需要提层数, 也就是将相应的节点提到 相应的其他层次去
        // 目前0层
        int currentLevel = 0;
        // 这里随机 结构 非常关键, 这里问题特别大
        // 如果这个随机数 小于 0.5 那么就要 提层数
        while (random.nextDouble() < 0.3d){
            // 判断是否大于层高
            if (currentLevel >= height){
                height++;
                Node dumyHead = new Node(null, HEAD_NODE);
                Node dumyTail = new Node(null, TAIL_NODE);

                dumyHead.right = dumyTail;
                dumyHead.down = head;
                head.up = dumyHead;

                dumyTail.left = dumyHead;
                dumyHead.down = tail;
                tail.up = dumyTail;

                head = dumyHead;
                tail = dumyTail;
            }
             while ((findNode != null) && findNode.up == null){
                  findNode = findNode.left;
             }
              findNode = findNode.up;

             Node upNode = new Node(element);
             upNode.left = findNode;
             upNode.right = findNode.right;
             upNode.down = newNode;

             findNode.right.left = upNode;
             findNode.right = upNode;
             newNode.up = upNode;

             newNode = upNode;
             currentLevel ++;
        }
        size++;
    }

    /**
     * 一层一层的取出来
     */
    public void dumpSkipList(){
        Node temp = head;
        int i = height+1;
        while (temp != null) {
            System.out.printf("Total [%d] height [%d]", height+1, i--);
            Node node = temp.right;
            while (node.bit == DATA_NODE){
                System.out.printf("->%d", node.value);
                node = node.right;
            }
            System.out.println("\n");
            temp = temp.down;
        }
        System.out.println("====================================");
    }


    /**
     *
     * @param element
     * @return
     */
    public boolean contains(Integer element){
        Node node = this.find(element);
        return  (node.value.equals(element));
    }

    /**
     *
     * @param element
     * @return
     */
    public  Integer get(Integer element){
        Node node = this.find(element);
        return  (node.value.equals(element)) ? node.value : null;
    }

    public int size(){
        return size;
    }

    /**
     * 看看元素是否为空
     * @return
     */
    public boolean isEmpty(){
        return (size() ==0);
    }


    public static void main(String[] args) {
        SimpleSkipList simpleSkipList = new SimpleSkipList();
        simpleSkipList.add(10);
        simpleSkipList.dumpSkipList();

        simpleSkipList.add(1);
        simpleSkipList.dumpSkipList();

        Random random = new Random();
        int count = 10;
        for (int i = 0; i < count ; i++) {
            simpleSkipList.add(random.nextInt(1000));
        }
        simpleSkipList.dumpSkipList();
    }





}
