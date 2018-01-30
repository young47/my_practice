package com.young.leetcode;

import java.util.LinkedList;

/**
 * Created by young on 17/10/11.
 */
public class ValidParentheses {
    public boolean isValid(String s) {
        LinkedList<Character> stack = new LinkedList<Character>();
        for(int i=0; i<s.length(); i++){
            char c = s.charAt(i);
            if(stack.isEmpty()){
                stack.push(Character.valueOf(c));
            }else if(isClose(stack.peek().charValue(), c)){
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    private boolean isClose(char c1, char c2){
        if(c1=='('&&c2==')' || c1=='['&&c2==']' || c1=='{'&&c2=='}'){
            return true;
        }
        return false;
    }
}
