package com.young.proxy.proxy_1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by young on 17/12/29.
 */
public class ProxyTest {

    ProxyTest(){
        super();
        System.out.println("123");
        //必须放在第一行
        //super();
    }
    synchronized final strictfp static public void main(String... args) {
        test();
    }

    private static void test() {
        Person developer = new Developer();
        Object o = Proxy.newProxyInstance(Person.class.getClassLoader(), new Class[]{Person.class}, new ProxyHandler(developer));
        ((Person) o).develop();

        System.out.println(Proxy.isProxyClass(o.getClass()));

        InvocationHandler handler = Proxy.getInvocationHandler(o);
        System.out.println(handler);
    }


    static class ProxyHandler implements InvocationHandler {
        private Object target;

        ProxyHandler(Object target) {
            this.target = target;
        }

        @Override
        public String toString() {
            return "ProxyHandler{" +
                    "target=" + target +
                    '}';
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("123456");
            return method.invoke(target, args);
        }
    }
}
