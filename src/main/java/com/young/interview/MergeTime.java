package com.young.interview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 比如有时间范围(0,10)(1,7)(16,20)(18,21) 输出 (0,10)(16,21)
 */
public class MergeTime {

    public static void main(String[] args) {
        ArrayList<Span> spans = buildSpans();
        List<Span> result = mergeSpan(spans);
        System.out.println(result);
    }

    private static ArrayList<Span> buildSpans() {
        ArrayList<Span> spans = new ArrayList<>(10);
        Span span = new Span(0, 10);
        spans.add(span);
        span = new Span(1, 7);
        spans.add(span);
        span = new Span(16, 20);
        spans.add(span);
        span = new Span(18, 21);
        spans.add(span);
        System.out.println(spans);
        return spans;
    }

    public static List<Span> mergeSpan(List<Span> spans) {
        ArrayList<Span> res = new ArrayList<>();
        Collections.sort(spans, new Comparator<Span>() {
            @Override
            public int compare(Span o1, Span o2) {
                return o1.left - o2.left;
            }
        });
        Span prev = null;
        for (Span span : spans) {
            if (prev == null || prev.right <= span.left) {//无交集
                res.add(span);
                prev = span;
            } else if (prev.right <= span.right) {//有交集
                prev.right = span.right;
            }
        }
        return res;
}

static class Span {
    int left;
    int right;

    public Span(int left, int right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + ", " + right + ")";
    }
}
}
