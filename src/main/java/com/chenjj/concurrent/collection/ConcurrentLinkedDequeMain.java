package com.chenjj.concurrent.collection;

import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentLinkedDequeMain {

  public static void main(String[] args) {
    ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();
    Thread[] threads = new Thread[100];
    for (int i = 0; i < threads.length; i++) {
      AddTask addTask = new AddTask(list);
      threads[i] = new Thread(addTask);
      threads[i].start();
    }
    System.out.printf("Main: %d AddTask threads have been launched\n", threads.length);

    // 等待这些线程的完成
    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.printf("Main: Size of the List: %d\n", list.size());

    for (int i = 0; i < threads.length; i++) {
      PollTask pollTask = new PollTask(list);
      threads[i] = new Thread(pollTask);
      threads[i].start();
    }
    System.out.printf("Main: %d PollTask threads have been launched\n", threads.length);

    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.printf("Main: Size of the List: %d\n", list.size());
  }

}
