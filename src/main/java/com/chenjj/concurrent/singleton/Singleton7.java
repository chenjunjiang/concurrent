package com.chenjj.concurrent.singleton;

/**
 * 枚举方式
 * 增加懒加载的特性
 * 在Singleton7初始化的时候并不会创建instance，只有调用getInstance的时候才会初始化
 */
public class Singleton7 {
    // 实例变量
    private byte[] data = new byte[1024];

    // 枚举的构造方法默认就是private
    private Singleton7() {
    }

    // 使用枚举类充当Holder
    private enum HolderEnum {
        INSTANCE;
        private Singleton7 instance;

        HolderEnum() {
            this.instance = new Singleton7();
        }

        private Singleton7 getSingleton7() {
            return instance;
        }
    }

    public static Singleton7 getInstance() {
        return HolderEnum.INSTANCE.getSingleton7();
    }
}
