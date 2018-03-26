package com.young.classloader.init;

/**
 * Created by young on 18/1/10.
 */
public class InitTest {
    public static void main(String... args) {
        //Asian并不能初始化，只初始化了Person
        //System.out.println(Asian.tag);
        //System.out.println(Asian.getTag());

        //都没有初始化
        //Asian[] asians = new Asian[10];

        // Person并没有初始化。
        // 因为age在编译阶段被放入了InitTest.class的常量池中，
        // 对age的调用，相当于对自身常量池中变量的引用，
        // 因此并不会触发Person类的初始化
        //System.out.println(Person.age);

        //new Asian();
        System.out.println(Asian.name);
    }
}

class Person {
    static {
        System.out.println("I am person");
        //tag = "abc";
    }

    public static String tag = "person";

    public static String getTag() {
        return tag;
    }

    public static final int age = 10;
}

class Asian extends Person {
    public static String name = "asian" ;
    static {
        System.out.println(name+"****");
        System.out.println("I am asian");
        //System.out.println(addr);
    }
    static String addr ;

    //代码块总是在构造器之前调用
    {
        System.out.println("Code block");
    }
    public Asian(){
        System.out.println("Asian constructor");
    }


    private static class Holder{
        static String hold = "bfv";
        static {
            System.out.println(hold);
        }
    }
}
