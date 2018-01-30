package com.young.xml;

/**
 * Created by young on 18/1/29.
 */
public class  EnumTest {
    public static void main(String... args){
        System.out.println(Singleton.getInstance().getClass());
    }
}

enum Singleton {
    INSTANCE;// 这里只有一项

    private String name;

    Singleton() {
        this.name = "Neo";
    }
    public static Singleton getInstance() {
        return INSTANCE;
    }

    public String getName() {
        return this.name;
    }
}
