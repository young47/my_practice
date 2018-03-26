package com.young.clone;

/**
 * Created by young on 18/1/2.
 */
public class CloneTest {
    public static void main(String[] args) throws CloneNotSupportedException{
        Person person = new Person(20, "John");
        System.out.println(person.getName());

        Person clone = (Person)person.clone();
        System.out.println(clone.getName());
        //System.out.println(person.equals(clone));


        System.out.println(person.getName()==clone.getName()?"Object.clone() is shallow copy":"Object.clone() is deep copy");
    }

    static class Person implements Cloneable {
        private int age;
        private String name;

        Person(int age, String name){
            this.age = age;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", name='" + name.hashCode() + '\'' +
                    '}';
        }
    }
}
