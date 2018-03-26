package com.young.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 17/12/18.
 */
public class OverLoadTest {
    public static void main(String[] args){
        //method(new ArrayList<String>());
        method(new ArrayList<Integer>());
    }

    /*public static String method(List<String> list){
        System.out.println("List<String> list");
        return "";
    }*/

    public static int method(List<Integer> list){
        System.out.println("List<Integer> list");
        return -1;
    }
}
