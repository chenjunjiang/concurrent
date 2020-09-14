# 不可变对象
  无论是synchronized关键字还是显示锁Lock，都会牺牲系统的性能，可以依赖于不可变对象的设计理念达到lock free(无锁)。
  所谓不可变对象是没有机会去修改它，每一次的修改都会导致一个新的对象产生。比如：java.lang.String。
1、不可变对象的设计原则：
   a、类添加final修饰符，保证类不被继承。
如果类可以被继承会破坏类的不可变性机制，只要继承类覆盖父类的方法并且继承类可以改变成员变量值，那么一旦子类以父类的形式出现时，不能保证当前类是否可变。
   b、保证所有成员变量必须私有，并且加上final修饰
通过这种方式保证成员变量不可改变。但只做到这一步还不够，因为如果成员变量是对象的话，是能在外部改变其值的。所以第4点弥补这个不足。
   c、不提供改变成员变量的方法，包括setter
避免通过其他接口改变成员变量的值，破坏不可变特性。
   d、通过构造器初始化所有成员，进行深拷贝(deep copy)
如果构造器传入的对象直接赋值给成员变量，还是可以通过对传入对象的修改进而导致改变内部变量的值。例如：
public final class ImmutableDemo {  
    private final int[] myArray;  
    public ImmutableDemo(int[] array) {  
        this.myArray = array; // wrong  
    }  
}
这种方式不能保证不可变性，myArray和array指向同一块内存地址，用户可以在ImmutableDemo之外通过修改array对象的值来改变myArray内部的值。
为了保证内部的值不被修改，可以采用深度copy来创建一个新内存保存传入的值。正确做法：
public final class MyImmutableDemo {  
    private final int[] myArray;  
    public MyImmutableDemo(int[] array) {  
        this.myArray = array.clone();   
    }   
}
   e、在getter方法中，不要直接返回对象本身，而是克隆对象，并返回对象的拷贝
这种做法也是防止对象外泄，防止通过getter获得内部可变成员对象后对成员变量直接操作，导致成员变量发生改变。

2、String的不可变性
  可以参考String的源码来验证上诉原则：
  public final class String
      implements java.io.Serializable, Comparable<String>, CharSequence
  {
      /** The value is used for character storage. */
      private final char value[];
      /** The offset is the first index of the storage that is used. */
      private final int offset;
      /** The count is the number of characters in the String. */
      private final int count;
      /** Cache the hash code for the string */
      private int hash; // Default to 0
      ....
      public String(char value[]) {
           this.value = Arrays.copyOf(value, value.length); // deep copy操作
       }
      ...
       public char[] toCharArray() {
       // Cannot use Arrays.copyOf because of class initialization order issues
          char result[] = new char[value.length];
          System.arraycopy(value, 0, result, 0, value.length);
          return result;
      }
      ...
  }
  String类被final修饰，不可继承
  string内部所有成员都设置为私有变量
  不存在value的setter
  并将value和offset设置为final
  当传入可变数组value[]时，进行copy而不是直接将value[]复制给内部变量
  获取value时不是直接返回对象引用，而是返回对象的copy