package com.young.extendsTest;

/**
 * Created by young on 17/11/28.
 */
public abstract class BeiJingPerson implements Person{
    public final void scheduleOfEveryDay(){
        wakeUp();

        wash();

        haveBreakFast();

        goToWork();
    }

    @Override
    public void haveBreakFast() {
        System.out.println("我爱喝豆汁。");
    }

    @Override
    public void goToWork(){
        chooseWay();
    }

    @Override
    public void wash(){
        washFace();
    }

    protected abstract void chooseWay();

    protected abstract void washFace();

    protected void method(){
        System.out.println("我是BeiJingPerson的方法");
    }

    public Number test(Number num){
        return null;
    }

    public Integer test1(Integer num){
        return null;
    }
}
