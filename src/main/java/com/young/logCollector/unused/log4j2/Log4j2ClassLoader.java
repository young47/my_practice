package com.young.logCollector.unused.log4j2;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import static com.sohu.log.util.Constants.BASE_PATH;

public class Log4j2ClassLoader extends URLClassLoader {

    public static ClassLoader getInstance() {
        return ClassLoaderHolder.log4j2ClassLoader;
    }

    private static class ClassLoaderHolder {
        private static final Log4j2ClassLoader log4j2ClassLoader = new Log4j2ClassLoader();
    }

    private Log4j2ClassLoader() {
        super(new URL[]{}, null);
        //super(new URL[]{});
        loadResources(BASE_PATH + File.separator + "lib");
        loadResources(BASE_PATH + File.separator + "lib/dependency");
    }

    private void loadResources(String libPath) {
        File jarBasePath = new File(libPath);
        if (jarBasePath.exists() && jarBasePath.isDirectory()) {
            for (File jarFile : jarBasePath.listFiles()) {
                if (jarFile.isFile()) {
                    try {
                        super.addURL(jarFile.toURI().toURL());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {

        }
    }

    @Override
    public String toString() {
        return "LogCollectorClassLoader";
    }
}
