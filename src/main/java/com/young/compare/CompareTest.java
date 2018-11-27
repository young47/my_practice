package com.young.compare;

import java.util.PriorityQueue;

public class CompareTest {
    public static void main(String[] args){
        PriorityQueue<Person> people = new PriorityQueue<Person>();
        Person tom = new Person("Tom", 10);
        Person jack = new Person("Jack", 4);
        Person john = new Person("John", 40);
        people.add(tom);
        people.add(jack);
        people.add(john);
        System.out.println(people);
    }

    private static class Person implements Comparable<Person>{
        private int age;
        private String name;
        public Person(String name, int age){
            this.name = name;
            this.age = age;
        }
        public int compareTo(Person o) {
            return this.age > o.getAge() ? 1 : this.age < o.getAge() ? -1 : 0;

        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
