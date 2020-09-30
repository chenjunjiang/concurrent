# Event Driven设计模式
  基于事件驱动的设计，总体来讲涉及如下几个重要的组件：
  1、事件消息(Event)
  一个Event至少需要包含两个属性：类型和数据，Event的类型决定了它会被哪个Handler处理，数据是在Handler中待加工的材料。
  2、针对事件的具体处理器(Handler)
  Handler主要用于处理Event，比如一些filter或transform数据的操作。
  3、接受事件消息的通道，比如queue
  4、分配事件消息的Event Loop
  处理所有收到的Event，并且将它们分配给合适的Handler去处理。