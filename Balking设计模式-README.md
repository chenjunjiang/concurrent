# Balking(犹豫)设计模式
  多个线程监控某个共享变量，A线程监控到共享变量发生变化后即将触发某个动作，但是此时发现有另外一个线程B已经针对该变量
的变化开始了行动，因此A便放弃了准备开始的工作，我们把这样的线程间交互称为Balking(犹豫)设计模式。
比如：我们在用word编写文档的时候，每次的文字编辑都代表着文档的状态发生了改变，除了我们可以使用ctrl+s快捷键手动保存
以外，word软件本身也会定期触发自动保存，如果word自动保存文件的线程准备执行保存动作的时候，恰巧我们进行了主动保存，
那么自动保存文档的线程将会放弃此次的保存工作。相关代码见例子。
  Balking模式在日常开发中很常见，比如在系统资源的加载或某些数据的初始化时，在整个系统生命周期中资源可能只被
加载一次，此时我们就可以采用Balking模式：
  public synchronized Map<String, Resource> load(){
      // balking
      if (loaded){
         return resourceMap;
      }
      // do the resource load task
      ......
      this.loaded = true;
      return resourceMap;
  }