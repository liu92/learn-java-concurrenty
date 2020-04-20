package com.learn.concurrenty.juc.collections.custom;

/**
 * @ClassName: MyQueue
 * @Description:
 * @Author: lin
 * @Date: 2020/4/20 17:14
 * History:
 * @<version> 1.0
 */
public class MyQueue<E> {
    private static class Node<E>{
        private E element;
        private Node<E> next;

        /**
         * 节点
         * @param element 元素
         * @param next  下一个节点
         */
        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }

        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return (element == null) ? "" : element.toString();
        }

    }

    private Node<E> first, last;
    private int size;

    public  int size(){
        return  size;
    }

    public boolean isEmpty(){
        return  size == 0;
    }

    /**
     * 第一个元素
     * @return
     */
    public E peekFirst(){
        return  first == null ? null : first.element;
    }

    /**
     * 最后一个元素
     * @return
     */
    public E peekLast(){
        return  last == null ? null : last.getElement();
    }

    /**
     * 添加元素
     * @param element
     */
    public void addLast(E element){
        Node<E> newNode = new Node<>(element, null);
        if(size() == 0){
            first = newNode;
        }else{
            last.setNext(newNode);
        }
        last = newNode;
        size ++;
    }

    public E removeFirst(){
        if(isEmpty()){return  null;}
        E element = first.getElement();
        first = first.getNext();
        size --;
        if(size() == 0){
            last = null;
        }
        return  element;
    }

    public static void main(String[] args) {
        MyQueue<String> myQueue = new MyQueue<>();
        myQueue.addLast("hello");
        myQueue.addLast("world");
        myQueue.addLast("java");

        System.out.println(myQueue.removeFirst());
        System.out.println(myQueue.removeFirst());
        System.out.println(myQueue.removeFirst());
    }
}
