package com.young.annotation;

import com.young.invoke.InvokeTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by young on 17/5/12.
 */
public class AnnotationTest {

    @Like
    public int age;

    private String name;

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        //AnnotationTest test = new AnnotationTest();
        //Field[] fields = test.getClass().getDeclaredFields();
        //for (Field field : fields) {
        //    System.out.println(field.getName()+"-------");
        //    Annotation[] annotations = field.getDeclaredAnnotations();
        //    for (Annotation annotation : annotations) {
        //        System.out.println(annotation.getClass().getName());
        //        System.out.println(annotation.getClass().getSimpleName());
        //        System.out.println(annotation.getClass().getName().equals(Like.class.getName()));
        //
        //    }
        //}

        //反射调用方法
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(100);
        try {
            Method[] methods = InvokeTest.class.getMethods();
            Method addIdNotIn = InvokeTest.class.getMethod("addIdNotIn", ArrayList.class);
            addIdNotIn.invoke(InvokeTest.class.newInstance(), new Object[]{list});
            //for (Method method : methods) {
            //    Object invoke = method.invoke(InvokeTest.class.newInstance(), (Object[])list.toArray());
            //}
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

