package com.chenjj.concurrent.containers.linkedList;

/**
 * 节点信息
 *
 * @param <T>
 */
public class Node<T> {
    // 数据
    private final T value;
    // 指向下一个节点的引用
    private final Node<T> next;

    public Node(T value, Node<T> next) {
        this.value = value;
        this.next = next;
    }

    public T getValue() {
        return value;
    }

    public Node<T> getNext() {
        return next;
    }
}
