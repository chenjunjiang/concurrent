# Latch(门阀)设计模式
  该模式指定了一个屏障，只有所有的条件都满足的时候，门阀才打开。
  比如：若干线程并发执行某个特定的任务，然后等到所有的子任务都执行结束之后再统一汇总。
  JDK提供了CountDownLatch工具类来实现这个功能。CountDownLatch只提供了门阀的功能，并不会负责对线程的管理控制，
对线程的控制还需要程序员自己控制。当await超时的时候，已完成任务的线程自然正常结束，但是未完成的则不会被中断还会
继续执行下去。我们自己实现的Latch逻辑也是这样的。