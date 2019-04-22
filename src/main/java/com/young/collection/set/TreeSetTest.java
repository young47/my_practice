package com.young.collection.set;

import java.util.PriorityQueue;
import java.util.TreeSet;

public class TreeSetTest {
    public static void main(String[] args) {
        TreeSet<Person> set = new TreeSet<>();
        Person yang = new Person("yang", 20, 10000);
        Person wang = new Person("wang", 30, 20000);
        set.add(yang);
        set.add(wang);

        Person xie = new Person("xie", 80, 40000);
        set.add(xie); //add fail

        Person liu = new Person("liu", 70, 20000);
        set.add(liu);
        Person liu2 = new Person("liu2", 70, 20000);

        System.out.println(set);
        System.out.println("=================");

        PriorityQueue<Person> queue = new PriorityQueue<>();
        queue.add(yang);
        queue.add(wang);
        queue.add(xie);
        queue.add(liu);
        queue.add(liu2);
        System.out.println(queue);
        Person p;
        while ((p = queue.poll()) != null) {
            System.out.println(p);
        }


    }

    static class Person implements Comparable<Person> {
        private String name;
        private int id;
        private int salary;

        public Person(String name, int id, int salary) {
            this.name = name;
            this.id = id;
            this.salary = salary;
        }

        @Override
        public int compareTo(Person p) {
            int s = this.salary - p.salary;
            return s == 0 ? this.id - p.id : s;
//            return this.salary > p.salary ? 1 : this.salary < p.salary ? -1 : 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return id == person.id;
        }

        @Override
        public int hashCode() {

            return id;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", salary=" + salary +
                    '}';
        }
    }
}
