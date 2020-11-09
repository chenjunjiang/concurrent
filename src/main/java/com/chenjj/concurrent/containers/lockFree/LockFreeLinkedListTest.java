package com.chenjj.concurrent.containers.lockFree;

/**
 * 在单线程环境下测试基本功能
 */
public class LockFreeLinkedListTest {
    public static void main(String[] args) {
        final LockFreeLinkedList<Integer> list = new LockFreeLinkedList<>();
        assert list.isEmpty();
        assert list.count() == 0;
        assert list.peekFirst() == null;
        assert list.removeFirst() == null;

        list.add(1);
        list.add(2);
        list.add(3);

        assert !list.isEmpty();
        assert list.count() == 3;
        assert list.removeFirst() == 3;
        assert list.removeFirst() == 2;
        assert list.count() == 1;

        list.clear();
        assert list.isEmpty();
        assert list.count() == 0;
        assert list.peekFirst() == null;
        assert list.removeFirst() == null;
    }
}
