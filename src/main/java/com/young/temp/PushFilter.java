package com.young.temp;

public interface PushFilter<T> {
    boolean accept(T t);
    int rejectNum();
}
