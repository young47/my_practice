package com.young.classloader.classStruct;

/**
 * Created by young on 18/1/12.
 */
public class TestClass {
    private int m;
    public int inc(){
        return m / 0;
    }
}
