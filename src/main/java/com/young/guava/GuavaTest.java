package com.young.guava;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuavaTest {
    public static void main(String[] args){
        StringBuilder sb = new StringBuilder();
        sb.append("|").append("aa");
        sb.append("|").append("bb");
        sb.append("|").append("cc");
        sb.append("|").append("dd");

        List<String> cids = Splitter.on("|").omitEmptyStrings().trimResults().splitToList(sb.toString());
        ArrayList<String> list = Lists.newArrayList();
        for (String cid : cids) {
            if (cid.equals("cc")){
//                cids.remove(cid);
            }
        }

        String join = Joiner.on("|").join(cids);
        System.out.println(join);

        System.out.println("6381521720150765690".hashCode()%10);

    }
}
