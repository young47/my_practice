package com.young.classloader.LoadManyModule;

import com.young.classloader.Module1;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ModuleClassLoader extends URLClassLoader {
    private static final String jarFile = "/Users/young/Desktop/programe/B.jar";

    private static final String targetClass = "com.young.classloader.Module1";

    public ModuleClassLoader(URL[] urls) {
        super(urls, null);
    }

    public static void main(String[] args) throws MalformedURLException, IllegalAccessException, InstantiationException,
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        URL url = new File(jarFile).toURI().toURL();
        System.out.println("url path=" + url.getPath());
        URL[] urls = {url};
        Class<?> clazz = load(urls);
        Class<?> clazz1 = load(urls);
        System.out.println("equal? " + clazz.equals(clazz1));

        Object o = clazz.newInstance();
        System.out.println(o instanceof Module1);


        System.out.println("++++++++++++++++++++++");
        ModuleClassLoader moduleClassLoader = new ModuleClassLoader(urls);
        Class<?> clazz2 = Class.forName(targetClass, true, moduleClassLoader);
        clazz2.getMethod("hello").invoke(clazz2.newInstance(), null);

        ModuleClassLoader moduleClassLoader3 = new ModuleClassLoader(urls);
        Class<?> clazz3 = Class.forName(targetClass, true, moduleClassLoader3);
        clazz3.getMethod("hello").invoke(clazz3.newInstance(), null);

        System.out.println("equal? " + clazz2.equals(clazz3));

        System.out.println("++++++++++++++++++++++");

    }

    private static Class<?> load(URL[] urls) {
        ModuleClassLoader moduleClassLoader = new ModuleClassLoader(urls);
        Class<?> clazz = null;
        try {
            clazz = moduleClassLoader.loadClass(targetClass);
            Method helloMethod = clazz.getMethod("hello");
            helloMethod.invoke(clazz.newInstance(), null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return clazz;
    }
}
