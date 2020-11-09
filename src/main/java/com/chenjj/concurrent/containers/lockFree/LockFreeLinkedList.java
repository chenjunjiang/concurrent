package com.chenjj.concurrent.containers.lockFree;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 能保证线程安全的无锁数据结构(链表)
 *
 * @param <E>
 */
public class LockFreeLinkedList<E> {
    /**
     * 由于要使得对链表头元素所有的操作均具备原子性，且是无锁的要求，因此我们使用AtomicStampedReference对Node
     * 节点进行原子性封装。还可以避免ABA的问题。
     */
    private AtomicStampedReference<Node<E>> headRef = null;

    public LockFreeLinkedList() {
        this.headRef = new AtomicStampedReference<>(null, 0);
    }

    /**
     * 链表为空，只需要判断头节点为null即可
     *
     * @return
     */
    public boolean isEmpty() {
        return this.headRef.getReference() == null;
    }

    /**
     * 清空链表，只需设置头节点的值为null即可， 其它节点由于和GC Roots没关联了，之后在GC的时候会被回收
     */
    public void clear() {
        this.headRef.set(null, headRef.getStamp() + 1);
    }

    /**
     * 只需要返回头节点的元素即可，如果链表为空，则返回null
     *
     * @return
     */
    public E peekFirst() {
        return isEmpty() ? null : this.headRef.getReference().element;
    }

    /**
     * 该方法效率比较低，需要遍历全部的元素才能计算出来，由于不加锁，在多线程的情况下，计算出的结果是近似值而不是精确值
     *
     * @return
     */
    public long count() {
        long count = 0;
        Node<E> currentNode = this.headRef.getReference();
        while (currentNode != null) {
            count++;
            currentNode = currentNode.next;
        }
        return count;
    }

    /**
     * 采用CAS+自旋的方式来保证多线程下链表数据的正确性
     *
     * @param element
     */
    public void add(E element) {
        if (element == null) {
            throw new NullPointerException("The element is null.");
        }
        // 旧节点
        Node<E> previousNode;
        // 旧Stamp
        int previousStamp;
        // 新节点
        Node<E> newNode;
        do {
            previousNode = this.headRef.getReference();
            previousStamp = this.headRef.getStamp();
            newNode = new Node<>(element);
            newNode.next = previousNode;
        } while (!this.headRef.compareAndSet(previousNode, newNode, previousStamp, previousStamp + 1));
    }

    /**
     * 删除头节点并返回头节点元素
     *
     * @return
     */
    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node<E> currentNode;
        int currentStamp;
        Node<E> nextNode;
        do {
            currentNode = this.headRef.getReference();
            currentStamp = this.headRef.getStamp();
            if (currentNode == null) {
                break;
            }
            // 删除头节点，就让头节点的下一个节点称为头节点
            nextNode = currentNode.next;
        } while (!this.headRef.compareAndSet(currentNode, nextNode, currentStamp, currentStamp + 1));
        return currentNode == null ? null : currentNode.element;
    }

    /**
     * 这里同样采用FILO（First-In-Last-Out）的栈结构（基于链表实现），因此在该链表中需要存在一个属性代表当前链表的头
     *
     * @param <E>
     */
    private static class Node<E> {
        E element;
        volatile Node<E> next;

        Node(E element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return element == null ? "" : element.toString();
        }
    }
}
