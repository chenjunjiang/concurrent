package com.chenjj.concurrent.collection;

import java.util.concurrent.ConcurrentLinkedDeque;

public class AddTask implements Runnable {

  /** 非阻塞线程安全列表 */
  private ConcurrentLinkedDeque<String> list;

  public AddTask(ConcurrentLinkedDeque<String> list) {
    this.list = list;
  }

  @Override
  public void run() {
    String name = Thread.currentThread().getName();
    for (int i = 0; i < 10000; i++) {
      list.add(name + ": Element " + i);
    }
  }

}
