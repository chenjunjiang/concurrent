# 线程上线文类加载器
   双亲委派模型很好的解决了各个类加载器的基础类的统一问题（越基础的类由越上层的加载器进行加载），如果基础类又要回调用户的类该怎么办？
一个非常经典的例子就是SQL的驱动管理类——java.sql.DriverManager。
java.sql.DriverManager是Java的标准服务，该类放在rt.jar中，因此是由启动类加载器(根类加载器)加载的，但是在应用启动的时候，
该驱动类管理是需要加载由不同数据库厂商实现的驱动，但是启动类加载器找不到这些具体的实现类，为了解决这个问题，
Java设计团队提供了一个不太优雅的设计：线程上下文加载器——Thread Context ClassLoader。
   这个类加载器可以通过java.lang.Thread类的setContextClassLoader()方法进行设置，如果创建线程时候它还没有被设置，
就会从父线程中继承一个，如果在应用程序的全局范围都没有设置过的话，那这个类加载器就是应用程序类加载器。
有了线程上下文加载器，就可以解决上面的问题——父类加载器需要请求子类加载器完成类加载的动作，这种行为实际上就是打破了双亲委派的加载规则。
源码分析可以参考ServiceLoader的load(Class<S> service)方法。

https://mp.weixin.qq.com/s?__biz=MzU5ODUwNzY1Nw==&mid=2247484936&idx=1&sn=32cb67042521bceb94ee5163e3e513e8&chksm=fe426feec935e6f84a964600c398fbecc34af5bce7f73ea81f4b80f16bf9baa6d31fbfabf277&mpshare=1&scene=23&srcid=&sharer_sharetime=1573519856389&sharer_shareid=58714c43bff8d32784ccc2cb555b2c13&client=tim&ADUIN=405702263&ADSESSION=1599113150&ADTAG=CLIENT.QQ.5585_.0&ADPUBNO=26849#rd
  