package com.young.jvm.OOM;

/**
 *
 * -XX:PermSize=10M -XX:MaxPermSize=10M JDK8中已取消了这两个参数
 * Hotspot中方法区已经使用 native memory 来实现，永久代已经被放弃
 *
 * <p>
 * VM args： -XX:MetaspaceSize=2M -XX:MaxMetaspaceSize=2M
 * <p>
 * 常量池撑爆了方法区
 * error: java.lang.OutOfMemoryError: Metaspace
 */
public class PermOOMTest {
    public static void main(String[] args) {
        //List<String> list = new ArrayList<>();
        int i = 0;
        while (true) {
            String s = i + "";
            //list.add(s);
            i++;
        }
    }
}
