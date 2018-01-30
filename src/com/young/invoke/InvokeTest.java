package com.young.invoke;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 17/5/19.
 */
public class InvokeTest {

    public static void main(String[] args) {
        ArrayList list = new ArrayList<>();
        list.add("a");
        list.add("b");
        addIdIn(list);
    }

    public static void addIdIn(List<Integer> ids){
        for (Integer id : ids) {
            System.out.println(id);
        }
    }

    public void addIdNotIn(List<Integer> ids){
        for (Integer id : ids) {
            System.out.println(id);
        }
    }
}
