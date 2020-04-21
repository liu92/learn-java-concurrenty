package com.learn.concurrenty.juc.collections;

/**
 * @ClassName: MyLinkedList
 * @Description: 自定义实现LinkedList
 * @Author: lin
 * @Date: 2020/4/16 9:28
 * History:
 * @<version> 1.0
 */
public class MyLinkedList<E> {

    private static final String PLAIN_NULL="null";
    /**
     * 因为是 单向的,所以第一个是first
     */
    private Node<E> first;

    /**
     * 定义一个空值
     */
    private final Node<E> NULL = (Node<E>) null;

    /**
     * 元素个数
     */
    private int size ;

    /**
     * 构造方法，初始化链表中第一个元素
     * 因为还没有插入数据，所以第一个元素是空值
     */
    public MyLinkedList(){
        this.first = NULL;
    }

    public int size(){
        return  size;
    }

    /**
     * 看看元素是否为空
     * @return
     */
    public boolean isEmpty(){
        return (size() ==0);
    }




    /**
     * 提供一个静态方法，添加多个元素
     * @param elements
     * @param <E>
     * @return
     */
    public static  <E> MyLinkedList<E> of(E ...elements){
        // 如果元素长度为0
        final  MyLinkedList<E> myLinkedList = new MyLinkedList<>();
        if(elements.length != 0){
            // 遍历去 将元素添加到这个list中去
            for (E e: elements) {
               myLinkedList.addFirst(e);
            }
        }
        return  myLinkedList;
    }

    /**
     * 添加元素
     * @param e
     * @return
     */
    public MyLinkedList<E> addFirst(E e){
        // 先声明一个Node,将元素放到节点中去
        final Node<E> newNode = new Node<>(e);
        // 因为链表中还没有元素,所以Node 的下一个元素就是first
        newNode.next = first;
        // 将元素个数++;
        size++;
        // 那么第一个元素就是 newNode
        this.first = newNode;
        // 返回当前对象
        return this ;
    }


    /**
     * 判断元素是存在
     * @param e
     * @return
     */
    public boolean contains(E e){
        //那么先从第一个节点开始
        Node<E> current = first;
        // 首先判断这元素是不是为空
        while (null != current ){
            if(current.value == e){
                //如果相等那么就返回true;
                return  true;
            }
            // 循环判断,第一个判断后不想等,那么就比较下一个
            // 将当前的下一个元素 取出来比较
            current = current.next;
        }
        return  false;
    }


    /**
     * 移除元素
     */
    public E removeFirst(){
        // 第一步，我们移除元素时,先判断元素链表中元素是否为空
        // 然后从第一个元素开始,就是后进先出的方式,
        // 这一点和出栈入栈的操作有点类似
        if(this.isEmpty()){
            // 可以抛出异常也可以 直接返回null
           throw  new NoElementException("The linked list is empty.");
        }
        // 将第一个元素放到临时节点中去
        Node<E> node = first;
        first = node.next;
        size -- ;
        // 返回移除元素的值,也就是移除了哪一个
        return node.value;
    }


    @Override
    public String toString() {
        if(this.isEmpty()){
            return  "[]";
        }else{
            StringBuilder builder = new StringBuilder("[");
            Node<E> current = first;
            while (current != null){
                builder.append( current.toString()).append(",");
                current = current.next;
            }
            // 当为空时 就是最后一个元素, 将builder中最后一个元素的符号进行替换
            builder.replace(builder.length()-1, builder.length(), "]");
            return  builder.toString();
        }
    }

    static class NoElementException extends RuntimeException{
        NoElementException(String message){
            super(message);
        }
    }

    // 单向链表: 每一个节点保留下一个节点的指针

    private static class  Node<E> {
        E  value;
        Node<E> next;
        public Node(E value){
            this.value = value;
        }

        @Override
        public String toString() {
            if(null != value){
                return  value.toString();
            }
            return PLAIN_NULL   ;
        }
    }


    public static void main(String[] args) {
        MyLinkedList<String> linkedList = MyLinkedList.of("Hello", "World", "Scala",
                "Java", "Thread");
        linkedList.addFirst("Current").addFirst("Test");
        System.out.println(linkedList.size());
        System.out.println(linkedList.contains("Test"));
        System.out.println("======================");
        System.out.println(linkedList);
        while (!linkedList.isEmpty()){
            System.out.println(linkedList.removeFirst());
        }
        System.out.println(linkedList.size());
        System.out.println(linkedList.isEmpty());
    }
}
