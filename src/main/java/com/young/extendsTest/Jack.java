package com.young.extendsTest;

/**
 * Created by young on 17/11/28.
 */
public class Jack extends BeiJingPerson {
    @Override
    public void wakeUp() {
        System.out.println("8点了，该起床了");
    }

    @Override
    protected void chooseWay() {
        System.out.println("挤地铁去咯");
    }

    @Override
    public void haveBreakFast() {
        System.out.println("XXXXXX");
    }

    @Override
    protected void washFace() {
        System.out.println("用手洗脸");
    }

    @Override
    protected void method(){
        System.out.println("Jack");
    }

    protected void method2(){
        System.out.println("Jack's method2");
    }

    /**
     * 子类的方法中，形参和父类必须相同；返回值可以是子类型
     * @param num
     * @return
     */
    @Override
    public Integer test(Number num) {
        return null;
    }

    @Override
    public Integer test1(Integer num) {
        return null;
    }
}
