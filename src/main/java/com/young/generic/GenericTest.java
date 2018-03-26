package com.young.generic;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 18/1/25.
 */
public class GenericTest<T> {
    private static Generic_2<Integer> gg = new Generic_2<Integer>();
    public static void main(String[] args) {

        Generic_2 g = new Generic_2<Integer>() {};
        gg.add(3);
        System.out.println(gg.get(0));
        TypeVariable<? extends Class<? extends Generic_2>>[] typeParameters = gg.getClass().getTypeParameters();
        TypeVariable<? extends Class<? extends Generic_2>> typeParameter = typeParameters[0];
        Type[] bounds = typeParameter.getBounds();
        for (Type bound : bounds) {
            System.out.println(bound.getTypeName());

        }
        System.out.println(typeParameter.getName());
/*
        ParameterizedType genericSuperclass = (ParameterizedType)g.getClass().getGenericSuperclass();
        System.out.println(genericSuperclass.getActualTypeArguments()[0].getTypeName());

        ParameterizedType genericSuperclass1 = (ParameterizedType)gg.getClass().getGenericSuperclass();
        System.out.println(genericSuperclass1.getActualTypeArguments()[0].getTypeName());*/
    }
}

class Generic_2<T>{
    List<String> list = new ArrayList<String>();
    List<T> list2 = new ArrayList<T>();

    public void add(T t) {
        //List<T> list3 = new ArrayList<T>();       // 1
        list2.add(t);
    }

    public T get(int index){
        return list2.get(index);
    }
}
