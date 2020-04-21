package com.learn.concurrenty.juc.collections;

/**
 * 优先级 链表
 * @ClassName: PriorityLinkedList
 * @Description:
 * @Author: lin
 * @Date: 2020/4/16 9:28
 * History:
 * @<version> 1.0
 */
public class PriorityLinkedList<E extends Comparable<E>>  {

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
    public PriorityLinkedList(){
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
    public static  <E extends  Comparable<E>> PriorityLinkedList<E> of(E ...elements){
        // 如果元素长度为0
        final PriorityLinkedList<E> myLinkedList = new PriorityLinkedList<>();
        if(elements.length != 0){
            // 遍历去 将元素添加到这个list中去
            for (E e: elements) {
               myLinkedList.addFirst(e);
            }
        }
        return  myLinkedList;
    }

    /**
     * 插入元素时候 去比较
     * 添加元素
     * @param e
     * @return
     */
    public PriorityLinkedList<E> addFirst(E e){
        // 先声明一个Node,将元素放到节点中去
        final Node<E> newNode = new Node<>(e);
        // 声明前面一个 节点
        Node<E> previous = NULL;
        // 要将前面的
        Node<E> current = first;
        // 当还没有到最后一个元素 并且 这个元素大于第一个
        //
        while (current!= null && e.compareTo(current.value) > 0){
            // 那么前一个元素就是第一个元素
             previous = current;
             current = current.next;
        }
        // 如果没有元素或者 比第一个都小
        if(previous == NULL){
            // 第一个元素就是这个新声明的
            first = newNode;
            // 将它的next 指向了 前一个的first
            //newNode.next = current;
        }else{
            // 找到了一个比当前元素大的 就要换一下 指向
            // 那么前一个元素的下一个 就是这个新定义的节点
            previous.next = newNode;
            // 这个新定义的节点的下一个 就是当前的 current;
            //newNode.next = current;
        }
        newNode.next = current;
        size ++;
        return  this;
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
        PriorityLinkedList<Integer> linkedList = PriorityLinkedList.of(10, 1, 20, 0, 4, -5, 100, 36, 89, 67);
        System.out.println(linkedList);
    }
}
