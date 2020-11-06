package com.chenjj.concurrent.containers.linkedList;

public class MyListTest {
    public static void main(String[] args) {
        MyList<Integer> list = new MyList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        System.out.println(list);
        System.out.println(list.size());
        System.out.println(list.isEmpty());
        System.out.println(list.peekFirst());
        System.out.println("=================================");
        System.out.println(list.popFirst());
        System.out.println(list.size());
        System.out.println(list);
        System.out.println(list.peekFirst());
        System.out.println(list.popFirst());
        System.out.println(list.popFirst());
        System.out.println(list.popFirst());
        System.out.println(list.popFirst());
        System.out.println("=================================");
        System.out.println(list.isEmpty());
        System.out.println(list.size());
        System.out.println(list);
    }
}
