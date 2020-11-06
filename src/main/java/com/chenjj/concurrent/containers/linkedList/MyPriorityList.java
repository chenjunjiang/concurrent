package com.chenjj.concurrent.containers.linkedList;

import java.util.Comparator;

/**
 * 增加对元素排序的支持，也就是说链表中的元素具备了某种规则下的优先级
 *
 * @param <E>
 */
public class MyPriorityList<E extends Comparable<E>> {
    private Node<E> header;
    private int size;
    private final Comparator<E> comparator;

    public MyPriorityList(Comparator<E> comparator) {
        this.comparator = comparator;
        this.header = null;
    }

    private static class Node<T extends Comparable<T>> {
        private T value;
        private Node<T> next;

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        public Node(T value) {
            this(value, null);
        }

        public T getValue() {
            return value;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

    /**
     * 每一个新元素的加入都需要进行比较，比较的目的当然是要在遍历已经存储于链表中的元素后，找到适当的位置将其加入
     *
     * @param e
     */
    public void add(E e) {
        // 创建新节点
        final Node<E> newNode = new Node<>(e);
        // 当前节点引用
        Node<E> current = this.header;
        // 上一个节点引用，初始值为null
        Node<E> precious = null;
        // 循环遍历链表(当前节点不为null，表示不是空的链表)
        while (current != null && e.compareTo(current.getValue()) > 0) {
            precious = current;
            current = current.getNext();
        }
        if (precious == null) {
            this.header = newNode;
        } else {
            precious.setNext(newNode);
        }
        newNode.setNext(current);
        this.size++;
    }
}
