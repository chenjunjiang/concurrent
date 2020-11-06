package com.chenjj.concurrent.containers.linkedList;

public class SkipListTest {
    public static void main(String[] args) {
        SkipList list = new SkipList();
        list.add(50);
        list.add(15);
        list.add(13);
        list.add(20);
        list.add(100);
        list.add(75);
        list.add(99);
        list.add(76);
        list.add(83);
        list.add(65);
        list.printList();
        System.out.println(list.getSize());
        list.search(50);
        list.remove(50);
        list.search(50);
        System.out.println(list.getSize());
    }
}
