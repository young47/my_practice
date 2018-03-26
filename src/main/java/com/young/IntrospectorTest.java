package com.young;

import java.beans.*;

/**
 * Created by young on 17/1/22.
 */
public class IntrospectorTest {
//    public boolean getIsPartitioin() {
//        return this.isPartitioin;
//    }

    public void setIsPartitioin(boolean isPartitioin) {
        this.isPartitioin = isPartitioin;
    }
//
//    public boolean isPartitioin() {
//        return this.isPartitioin;
//    }

    private boolean isPartitioin;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public static void main(String[] args) throws IntrospectionException {
        IntrospectorTest demo = new IntrospectorTest();
        BeanInfo beanInfo = Introspector.getBeanInfo(demo.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor:propertyDescriptors) {
            System.out.println("get : "+propertyDescriptor.getReadMethod());
            System.out.println("set : "+propertyDescriptor.getWriteMethod());

        }


    }
}
