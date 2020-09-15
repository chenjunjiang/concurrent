# Class.forName 和 ClassLoader的区别
  查看GetCallerClassTest和MyClassLoaderTest，通过两者的使用方式就能看出区别。
  还有这篇文章，通过mysql的jdbc driver讲解了为什么它必须使用Class.forName?
  https://mp.weixin.qq.com/s?__biz=MzI1NTI3MzEwMg==&mid=2247494433&idx=1&sn=2aea16ccb176343a22fa4961bcf6a2dd&chksm=ea3adac0dd4d53d61bccabe2281ef67b2c3cd39ded52a69761a49b868fb3452dd4167c31125f&mpshare=1&scene=24&srcid=0915TMCWMG8sxbV3KGtoJWLI&sharer_sharetime=1600130818613&sharer_shareid=58714c43bff8d32784ccc2cb555b2c13#rd
  因为在JDBC规范中明确要求Driver(数据库驱动)类必须向DriverManager注册自己。
  以MySQL的驱动为例解释：
  public class Driver extends NonRegisteringDriver implements java.sql.Driver {  
      // ~ Static fields/initializers  
      // ---------------------------------------------  
  
      //  
      // Register ourselves with the DriverManager  
      //  
      static {  
          try {  
              java.sql.DriverManager.registerDriver(new Driver());  
          } catch (SQLException E) {  
              throw new RuntimeException("Can't register driver!");  
          }  
      }  
  
      // ~ Constructors  
      // -----------------------------------------------------------  
  
      /** 
       * Construct a new driver and register it with DriverManager 
       *  
       * @throws SQLException 
       *             if a database error occurs. 
       */  
      public Driver() throws SQLException {  
          // Required for Class.forName().newInstance()      }  
  
  }
  由于是在静态块中registerDriver，我们知道只有在类初始化的时候才会执行静态块，所以在写JDBC时必须使用Class.forName("")。