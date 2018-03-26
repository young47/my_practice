package com.young.designPattern.createPattern;


/**
 * 创建型模式
 * 1，简单工厂模式
 *    一般是一个工厂，生产不同型号的物品；
 * 2，工厂模式
 *    对简单工厂模式进行了扩展；可以选择多个不同的工厂，比如FoodFactory，ComputerFactory
 *
 * 3，抽象工厂模式
 *    当涉及到产品簇的时候，会引入该模式；
 */
public class FactoryTest {
    static class Computer{
        private String name;
        public void setName(String name){
            this.name = name;
        }
    }
    public static class FoodFactory{
        public static Computer getCompuper(String name){
            return null;
        }
    }
}
