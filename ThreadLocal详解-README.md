# ThreadLocal详解
  ThreadLocal为每一个使用该变量的线程提供了独立的副本，可以做到线程间的数据隔离，每一个线程都可以访问各自内部的副本变量。
  1、ThreadLocal的使用场景及注意事项
     a、在进行对象跨层传递的时候 ，可以考虑使用ThreadLocal，避免方法间多次传递，打破层级间的约束。
     b、线程间数据隔离。
     c、进行事务操作，用于存储线程事务信息。
  ThreadLocal并不是解决多线程下共享资源的技术，一般情况下，每一个线程的ThreadLocal存储的都是一个全新的对象，如果多线程的ThreadLocal
存储了一个对象的引用，那么它将面临资源竞争、数据不一致等并发问题。
  
  2、ThreadLocal内存泄露分析
  https://blog.csdn.net/thewindkee/article/details/103726942
  代码见：ThreadLocalTest