package com.young.designPattern.createPattern;

/**
 * Created by young on 18/1/15.
 */
public class SingletonTest {

}

//饿汉
class Singleton_1 {
    private Singleton_1() {
    }

    //instance可能没有使用，但是依然会初始化，这姑且算一个缺点
    private static Singleton_1 instance = new Singleton_1();

    public static Singleton_1 getInstance() {
        return instance;
    }
}

//饿汉改进
class Singleton_2 {
    private Singleton_2() {
    }

    //如果没有使用Holder，就不会初始化
    private static class Holder {
        private static Singleton_2 instance = new Singleton_2();
    }

    public static Singleton_2 getInstance() {
        //会使Holder初始化
        return Holder.instance;
    }
}

//饱汉
class Singleton_3 {
    private Singleton_3() {
    }

    private static volatile Singleton_3 instance = null;

    public static Singleton_3 getInstance() {
        //第一层if，可以提高效率；否则所有的线程都要排队获取锁了
        if (instance == null) {
            synchronized (Singleton_3.class) {
                //第二层if，保证了多线程访问的正确性；否则不同的线程可能获取不同的instance
                if (instance == null) {
                    instance = new Singleton_3();
                }
            }
        }
        return instance;
    }
}
