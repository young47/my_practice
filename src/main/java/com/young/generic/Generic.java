package com.young.generic;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by young on 17/9/5.
 */
public class Generic {
    public static void main(String[] args) {
        //test_1();
        //test_2();
        //test_3();
        test_4();
        genericArrayTest();


        List<Number> list11 = new ArrayList<Number>();
        list11.add(new Integer(1));

        List<? super Number> list = new ArrayList<Number>();
        list.add(new Integer(1));
        list.add(new Float(1.2f));

        List<? super Integer> list1 = new ArrayList<Number>();
        list1.add(new Integer(1));
        //list1.add(new Long(1));
        List<? super A> list4 = new ArrayList();
        list4.add(new B());

        //List<? super Number> list2 = new ArrayList<Integer>();
        //list2.add(new Integer(2));

        List<String>[] lsa = new List[10]; // Not really allowed.
        Object[] oa = new List[10];
        /*List<? super B> list4 = new ArrayList<A>();



        List list5 = new ArrayList<B>();
        list5.add(new A());
        list5.add(new B());
        Object o = list5.get(0);
        if (o instanceof A) {
            ((A) o).say();
        }
        o = list5.get(1);
        if (o instanceof B) {
            ((A) o).say();
        }*/

        List<?> list6 = new ArrayList<B>();

        //list1.add(new A());  //报错
        List<? super HashMap> list655 = new ArrayList<Map>();
        //list655.add(new Integer(1));
    }

    /**
     * 不能创建泛型数组
     */
    private static void genericArrayTest() {
        ArrayList<String>[] array = new ArrayList[10];
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        array[0] = list1;
        array[1] = list2;
    }

    private static void test_4() {
        System.out.println("---test_4---");
        List<String> list = new ArrayList<>();
        isArrayList(list);
        List<String> list1 = new LinkedList<>();
        isArrayList(list1);
        List<? extends A> list2 = new ArrayList<>();
        isArrayList(list2);

    }

    private static void test_3() {
        System.out.println("---test_3---");
        List<? super B> list = new ArrayList<A>();
        list.add(new B());
        Object object = list.get(0);
        //((B) object).say();
        //list.add(new A()); //该语句报错。
        // 原因： List<? super B> list 表示其中的元素是B的某个父类，将子类赋给父类是没问题的。
        // 但是若B有多个父类，则 add(A)就是错的。
        // 一般我们像这样定义一个list，意思是该list可以存储某个父类，而不是为了将任意父类都add进去
        //
        List<A> list2 = new ArrayList();
        addElement(list2, new A());
        list2.get(0).say();
        addElement(list2, new B());
        list2.get(1).say();

        List<B> list3 = new ArrayList();
        addElement(list3, new B());
        //addElement(list3, new A());

        List<C> list4 = new ArrayList();
        //addElement1(list4, new B());
    }

    private static void test_2() {
        System.out.println("---test_2---");
        ArrayList<A> list = new ArrayList();
        list.add(new A());
        A a = list.get(0);
        a.say();
        List<? extends A> list1 = Lists.newArrayList(new A(), new B());
        //list1中不能add, 只能get
        A a1 = list1.get(1);
        a1.say();
    }

    private static void test_1() {
        List list = new ArrayList();
        list.add(123);
        list.add(new A());
        list.add(new B());
        Object o = list.get(0); //o到底什么类型？不清楚

        List<B> list1 = new ArrayList();
        list1.add(new B());
        //list1.add(new A()); //error

        List<A> list2 = new ArrayList();
        list2.add(new A());
        list2.add(new B());

        List<? extends A> list3 = new ArrayList();
        //list3.add(new B());
        //list3.add(new A());
        list3.get(0);

        List<? super B> list4 = new ArrayList();
        list4.add(new B());
        //list4.add(new A());
        Object object = list4.get(0);
    }

    static class C{}
    static class A {
        public void say() {
            System.out.println("I am A");
        }
    }

    static class B extends A {
        public void say() {
            System.out.println("I am B");
        }
    }

    private static <T> void addElement(List<? super T> list, T t){
        list.add(t);
    }

    private static void addElement1(List<? super B> list, B t){
        list.add(t);
    }
    private static <T> void isArrayList(List<T> list){
        /*if (list instanceof ArrayList<Number>){//会擦除掉，不能对确切的泛型使用

        }*/
        if (list instanceof ArrayList<?>){//这个不会类型擦除？此时?
            System.out.println(true);
        }else {
            System.out.println(false);
        }
    }

    //public <T> void test(List<T> list){}
    public void test(List<?> list){}
}
