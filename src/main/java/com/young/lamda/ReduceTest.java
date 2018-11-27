package com.young.lamda;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ReduceTest {
    private static Set<String> set1 = Sets.newHashSet(new String[]{"10","90","100"});
    private static Set<String> set2 = Sets.newHashSet(new String[]{"100","190","1100"});
    private static Set<String> set3 = Sets.newHashSet(new String[]{"190","89","100"});

    public static Set<String> retain(List<Set<String>> sets){
        Optional<Set<String>> reduce = sets.stream().parallel().reduce((f, l) -> {
            f.retainAll(l);
            return f;
        });
        return reduce.get();
    }

    public static void main(String[] args){
        List<Set<String>> sets = Lists.asList(set1, new Set[]{set2, set3});
        System.out.println(retain(sets));
    }
}
