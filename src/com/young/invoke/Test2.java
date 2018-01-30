package com.young.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 17/5/19.
 */
public class Test2 {


    /**
     * 补充说明
     * 一个方法中的参数为print(Object ... objects),
     * printf(String ... strings)等方法这样形式的时候有两种传递参数的方法
     * a、Object[] objects = {1,2,3,"23"};
     * print(objects);
     * b、print(1,2,3,4,"23");
     * <p>
     * 注意:
     * 一个方法中只能出现一次这样的形式，Object ... objects，而且这个必须是最后的那个位置。
     * <p>
     * 使用反射调用类中的方法
     * 本例主要实现的功能有
     * 1、没有参数的方法
     * 2、一个参数的方法，没有返回值的方法
     * 3、多个参数的方法
     * 4、参数为数组的方法
     * 5、参数为集合的方法
     * 6、返回值为数组的方法
     *
     * @author 2014-11-5 上午10:51:28
     * @version V1.0
     * @modify by user: {修改人} 2014-11-5
     * @modify by reason:{方法名}:{原因}
     */

    class User {

        /**
         * 普通形式
         */
        String name = "defalutName";

        /**
         * 数组的形式
         */
        String[] nameArray;

        /**
         * 集合的形式
         */
        List<String> list;

        /**
         * 1、没有参数的方法
         *
         * @return
         * @author 2014-11-7 上午11:00:46
         * @modificationHistory=========================逻辑或功能性重大变更记录
         * @modify by user: {修改人} 2014-11-7
         * @modify by reason:{原因}
         */
        public String getName() {
            return name;
        }

        /***
         *  2、一个参数的方法,没有返回值的方法
         * @author 2014-11-7 上午11:01:17
         * @param name
         * @modificationHistory=========================逻辑或功能性重大变更记录
         * @modify by user: {修改人} 2014-11-7
         * @modify by reason:{原因}
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 3、多个参数的方法
         *
         * @param name
         * @param id
         * @author 2014-11-7 上午10:59:28
         * @modificationHistory=========================逻辑或功能性重大变更记录
         * @modify by user: {修改人} 2014-11-7
         * @modify by reason:{原因}
         */
        public void print(String name, Integer id) {
            System.out.println("id: " + id + " name: " + name);
        }

        /***
         *  4、参数为数组的方法
         * @author 2014-11-7 上午11:01:58
         * @param nameArray
         * @modificationHistory=========================逻辑或功能性重大变更记录
         * @modify by user: {修改人} 2014-11-7
         * @modify by reason:{原因}
         */
        public void setNameArray(String[] nameArray) {
            this.nameArray = nameArray;
        }

        /**
         * 5、参数为集合的方法
         *
         * @param list
         * @author 2014-11-7 上午11:03:26
         * @modificationHistory=========================逻辑或功能性重大变更记录
         * @modify by user: {修改人} 2014-11-7
         * @modify by reason:{原因}
         */
        public void setList(List<String> list) {
            this.list = list;
        }

        /**
         * 6、返回值为数组的方法
         *
         * @return
         * @author 2014-11-7 上午11:04:08
         * @modificationHistory=========================逻辑或功能性重大变更记录
         * @modify by user: {修改人} 2014-11-7
         * @modify by reason:{原因}
         */
        public String[] getNameArray() {
            return nameArray;
        }

        public List<String> getList() {
            return list;
        }
    }

    /**
     * 调用对象的方法
     *
     * @param object           要操作的对象
     * @param methodName       方法名称
     * @param parameterClasses 参数的Class数组
     * @param methodParameters 具体的方法参数值
     * @return 方法调用后返回的对象，如果没有返回值则为null
     * @author 2014-11-8 上午11:17:46
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2014-11-8
     * @modify by reason:{原因}
     */
    private static Object invokeMethod(Object object, String methodName, Object[] methodParameters, Class... parameterClasses) {

        Object result = null;

        try {
            //参数为null的情况底层已经处理了
            //获取某个类的方法时，有多个参数的时候使用new Class[]{}数组的形式
            Method method = object.getClass().getMethod(methodName, parameterClasses);

            //调用某个类的方法时，有多个参数的时候或者参数为集合或数组的时候，参数值必须使用new Object[]{}数组的形式传递进去。
            result = method.invoke(object, methodParameters);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {

            return result;
        }

    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {

        Test2 testReflectMethod = new Test2();

        /**0、实例化内部类对象***/
        User user = testReflectMethod.new User();

        /**1、没有参数的方法*/
        String name = (String) invokeMethod(user, "getName", null);

        System.out.println("1、没有参数有返回值的方法  name: " + name);

        String param = "Tom";

        /**2、有一个参数且没有返回值的方法*/
        Object noReturn = invokeMethod(user, "setName", new Object[]{param}, String.class);

        System.out.println("2、有一个参数但没有返回值的方法，则调用后返回值为null " + noReturn);

        /**3、多个参数的方法*/
        Object noReturnObject = invokeMethod(user, "print", new Object[]{"Name_2", 2}, new Class[]{String.class, Integer.class});

        System.out.println("3、多个参数的方法 " + noReturnObject);

        /**4、参数为数组的方法*/
        String[] arrString = {"A", "B"};
        //这个警告是因为方法中的参数使用的是...的形式。这个如果使用("A","B")的形式就不会出现了.
        //当参数为数组或者集合的时候必须使用new Object[]{}的方式
        invokeMethod(user, "setNameArray", new Object[]{arrString}, String[].class);

        System.out.println("4、参数为数组的方法");

        /**5、参数为集合的方法*/
        List<String> list = new ArrayList<String>();
        list.add("list1");
        list.add("list2");
        //先添加进去值
        invokeMethod(user, "setList", new Object[]{list}, List.class);
        //获取值
        List<String> returnList = (List<String>) invokeMethod(user, "getList", null);

        System.out.println("5、参数为集合的方法" + returnList.toString());

        /**6、返回值为数组的方法*/
        //返回的为Object所以需要强制转换
        String[] returnStringArray = (String[]) invokeMethod(user, "getNameArray", null);

        System.out.println("6、返回值为数组的方法" + returnStringArray.length);

    }
}

