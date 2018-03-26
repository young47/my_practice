package com.young.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Created by young on 18/1/25.
 */
public class GetGenericInfo {
    public static void main(String[] args){
        //extends
        Type type = B.class.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        for (Type type1 : parameterizedType.getActualTypeArguments()) {
            System.out.println(type1);
        }

        //implements
        ParameterizedType parameterizedType1 = (ParameterizedType) D.class.getGenericInterfaces()[0];//0代表只有一个接口
        for (Type type1 : parameterizedType1.getActualTypeArguments()) {
            System.out.println(type1);
        }

        TypeVariable<? extends Class<? extends A>>[] typeParameters = new A().getClass().getTypeParameters();
        for (TypeVariable<? extends Class<? extends A>> typeParameter : typeParameters) {
            System.out.println(typeParameter.getTypeName());
        }
    }
}


class A<K,V>{
    public void getType(){
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        for (Type type1 : parameterizedType.getActualTypeArguments()) {
            System.out.println(type1);
        }
    }
}
class B extends A<String, Integer>{
}

interface C<T>{}
class D implements C<String>{}
