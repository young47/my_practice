package com.young.enum1;

public class EnumTest {
    public static void main(String[] args){
        Action action = Action.valueOf("create");
        System.out.println(action.name());
    }

    private enum Action {

        CREATE,
        MODIFY,
        DELETE;
    }
}
