package com.young.dataStructure;

public class SkipList {
    private final int maxLevel = 32;

    //用来计算节点的层
    private final double p = 0.30f;

    private Node head = null;
    private int size = 0;

    public void insert(String name, int age) {
        int levels = randomLevels();
        Node newNode = new Node(name, age, levels);
        if (head == null) {
            head = newNode;
        } else {
            Node[] updates = new Node[levels];
            Node curr = head;
            for (int i = levels - 1; i >=0 ; i--) {
                while (curr.forwards[i] != null && curr.forwards[i].age < age){
                    curr = curr.forwards[i];
                }
                updates[i] = curr; //updates保存了需要更新0到levels层forwards的节点
            }
            //更新
            for (int i = 0; i < levels; i++) {
                newNode.forwards[i] = updates[i].forwards[i];
                updates[i].forwards[i] = newNode;
            }

        }
        size++;
    }

    public Node find(String name, int age) {
        Node curr = head;
        while (curr != null) {
            if (curr.age > age) {
                return null;
            }
            if (curr.age == age) {
                return curr;
            }
            for (int i = curr.levels - 1; i >= 0; i--) {
                if (curr.forwards[i].age < age) {
                    curr = curr.forwards[i];
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 如果一个节点包含第i层指针(意味着1到层都包含该节点)，则该节点包含第(i+1)层指针的概率为p
     */
    private class Node {
        private String name;
        private int age;
        private int levels;

        //不是仅有一个next指针
        private Node[] forwards;

        //为了方便，也添加一个next；next实际上是最下层的forward
        //private Node next;

        public Node(String name, int age, int level) {
            this.name = name;
            this.age = age;
            this.forwards = new Node[level];
            this.levels = level;
        }
    }

    private int randomLevels() {
        int level = 1;
        while (Math.random() < p && level < maxLevel) {
            level += 1;
        }
        return level;
    }

}
