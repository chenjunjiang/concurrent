package com.chenjj.concurrent.containers.linkedList;

/**
 * 链表中有一个非常重要的元素Head，它代表当前节点元素的引用，当链表被初始化时，当前节点属性指向为NULL
 */
public class MyList<E> {
    // 当前节点引用
    private Node<E> header;
    // 链表元素的个数
    private int size;

    public MyList() {
        this.header = null;
    }

    /**
     * 链表是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return this.header == null;
    }

    /**
     * 清空链表
     */
    public void clear() {
        this.size = 0;
        // 将当前节点引用设置为null，由于其它元素ROOT不可达，因此会在垃圾回收的时候被回收
        this.header = null;
    }

    /**
     * 向链表头部添加元素
     *
     * @param e
     */
    public void add(E e) {
        // 创建一个新节点
        Node<E> node = new Node<>(e, header);
        this.header = node;
        this.size++;
    }

    /**
     * peek操作不会对当前链表产生任何副作用，其只是简单地返回当前链表头部的数据。
     * 当然了，如果链表为空，则会抛出异常（为什么要抛出异常呢？返回null可不可以呢？
     * 其实这并没有什么不妥，链表可以存放任何类型的数据，当然它也可以存放值为null的数据，
     * 如果当前节点引用的value值恰巧为null，那么当peek操作在链表为空的情况下也返回null则势必会产生一些歧义，
     * 因此直接抛出异常将更加直观一些）。
     *
     * @return
     */
    public E peekFirst() {
        //如果为空则抛出异常
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("The linked list is empty now, can't support peek operation");
        }
        return header.getValue();
    }

    /**
     * 链表元素弹出
     * 本例其实更多地像是使用链表实现了一个栈的数据结构，因此当栈头元素弹出时，
     * 只需要让链表当前节点的引用指向已弹出节点的下一个节点即可
     *
     * @return
     */
    public E popFirst() {
        //如果为空则抛出异常
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("The linked list is empty now, can't support peek operation");
        }
        final E value = header.getValue();
        this.header = header.getNext();
        this.size--;
        return value;
    }

    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        Node<E> node = this.header;
        final StringBuilder builder = new StringBuilder("[");
        while (node != null) {
            builder.append(node.getValue().toString()).append(",");
            node = node.getNext();
        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");
        return builder.toString();
    }
}
