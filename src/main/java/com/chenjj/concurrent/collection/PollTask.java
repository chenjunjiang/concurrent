package com.chenjj.concurrent.collection;

import java.util.concurrent.ConcurrentLinkedDeque;

public class PollTask implements Runnable {

  /** 非阻塞线程安全列表 */
  private ConcurrentLinkedDeque<String> list;

  public PollTask(ConcurrentLinkedDeque<String> list) {
    this.list = list;
  }

  @Override
  public void run() {
    for (int i = 0; i < 10000; i++) {
      list.pollFirst();
      list.pollLast();
    }
  }

}
