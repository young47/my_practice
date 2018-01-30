package com.young.string;

/**
 * jdk1,6常量池放在方法区，jdk1.7常量池放在堆内存，jdk1.8放在元空间里面，和堆相独立
 * http://blog.csdn.net/seu_calvin/article/details/52291082
 */
public class InternTest {
    public static void main(String[] args){
        //test_1();
        //test_2();
        test_3();

    }

    /**
     * str1, str2, str3和intern()指向常量池
     * str4, str5, str6指向堆
     */
    private static void test_1() {
        String str1 = "a";
        String str2 = "b";
        String str3 = "ab";
        String str4 = str1 + str2;
        String str5 = new String("ab");
        String str6 = new String("a") + new String("b");


        System.out.println(str5.equals(str3));
        System.out.println(str4 == str3); //false
        System.out.println(str5 == str3); //false
        System.out.println(str6 == str3); //false
        System.out.println(str5 == str4); //false
        System.out.println(str6 == str4); //false
        System.out.println(str6.intern() == str4); //false
        System.out.println(str5.intern() == str4); //false

        System.out.println(str4.intern() == str4); //false
        System.out.println(str5.intern() == str5); //false
        System.out.println(str6.intern() == str6); //false


        System.out.println(str6.intern() == str3); //true
        System.out.println(str5.intern() == str3); //true
        System.out.println(str4.intern() == str3); //true
        System.out.println(str5.intern() == str6.intern()); //true

    }

    /**
     * Jdk1.6后，常量池移到了堆中
     * 不同版本中intern()的不同之处在于：当堆中有某个string，比如11，但常量池中没有时，调用intern(),老版本会直接在常量池创建11；
     * 新版本不会创建，而是保存堆中指向11的地址；
     * 一言以蔽之，堆中有而常量池无，那么常量池中保存堆中的地址
     */
    private static void test_2() {
        String s = new String("1");//堆中创建了一个对象，常量池中放入了1
        String intern = s.intern();//检查常量池中是否有1，此时有1，什么也没做
        String s2 = "1"; //会在常量池中创建1，但是此时已经存在1了，s2就指向了这个地址
        System.out.println(s == s2);//s指向堆，s2指向常量池，肯定是不同的

        String s3 = new String("1") + new String("1");//堆中创建了对象，并在常量池里放入1
        //区别提现在这里。！！！！！！！！！！！！！！！！！！！！
        s3.intern();//检查常量池中是否有11，此时没有，jdk1.6的做法是直接创建11；jdk7没有直接创建，而是存储了s3引用的对象地址。这里不是很明白，是在常量池中保存的这个地址？
        String s4 = "11";//去常量池创建11，发现已有，s4指向了这个地址
        System.out.println(s3 == s4);//true
    }

    private static void test_3() {
        String s = new String("1");//堆中创建了一个对象，常量池中放入了1
        String s2 = "1"; //发现堆中有1，s2指向这里
        s.intern(); //无用，因为堆中已有1了
        System.out.println(s == s2); //false

        String s3 = new String("1") + new String("1");//堆中创建了对象，并在常量池里放入1
        String s4 = "11";//去常量池创建11，发现没有，直接创建，s4指向了这个地址
        s3.intern();//无用了
        System.out.println(s3 == s4);//false
    }
}
