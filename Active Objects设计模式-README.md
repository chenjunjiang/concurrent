# Active Objects设计模式
  Active是“主动”的意思，Active Object是“主动对象”的意思，所谓主动对象就是指其拥有自己的独立线程，比如java.lang.Thread
实例就是一个主动对象，不过Active Object Pattern不仅仅拥有独立的线程，它还可以接受异步消息，并且能够返回处理的结果。
  System.gc()方法就是一个“接受异步消息的主动对象”，调用gc方法的线程和gc自身的执行线程并不是同一个线程。
  
1、标准的Active Objects
   接口方法的调用和接口方法的执行是在不同的线程中运行，因此我们称该接口为Active Objects(可接受异步消息的主动对象)，我们需要将接口方法
的参数以及具体实现(比如例子中的OrderServiceImpl)封装成特定的Message告知执行线程。
   标准的Active Objects需要将每一个方法都封装成Message(FindOrderDetailsMessage,OrderMessage)，然后提交至Message队列中，这种做法
有点类似于远程方法调用(RPC)。如果接口的方法很多，那么就就需要封装很多的Message类。如果接口变多，那需要封装很多的Message类就更多了，
这种设计就显得不是很友好了。所以我们需要设计一个更为通用的Active Objects。

2、通用的Active Objects
   我们基于JDK的动态代理来实现。可以将任意接口方法转换为Active Objects，当然如果接口方法有返回值，则必须要求返回Future类型才可以。