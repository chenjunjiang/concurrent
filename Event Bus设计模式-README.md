# Event Bus设计模式
  Event Bus设计模式的思想和消息中间件类似，主要用于解决进程间消息异步处理。我们可以用这种思想设计一个Java进程内部的
消息中间件-Event Bus。
  Event Bus设计中有三个非常重要的角色：Bus、Registry和Dispatcher，Bus主要提供给外部使用的操作方法，Registry注册表
用来整理记录所有注册在Bus上的Subscriber，Dispatcher主要负责对消息进行推送(用反射的方式执行方法)，但是考虑到程序
的灵活性，Dispatcher方法中又提供了Executor的多态方式。