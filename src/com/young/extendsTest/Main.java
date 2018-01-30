package com.young.extendsTest;

/**
 * Created by young on 17/11/28.
 */
public class Main {
    public static void main(String[] args){
        BeiJingPerson person = new Jack();
        ((BeiJingPerson)person).scheduleOfEveryDay();
        person.method();  //可以访问

        JackSon jackSon = new JackSon();
        jackSon.method();

        Jack jack = new Jack();
        jack.method();
        jack.wash();

        Jack jackSon1 = new JackSon();
        jackSon1.method();
        jackSon1.method2();
        ((Jack)jackSon1).method();
        ((Jack)jackSon1).washFace();
    }
}
