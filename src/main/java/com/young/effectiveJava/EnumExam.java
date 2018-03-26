package com.young.effectiveJava;

/**
 * Created by young on 17/10/11.
 */
public enum EnumExam {
    SUNDAY(PayType.WEEKEND), MONDAY(PayType.WEEKDAY);
    private final PayType payType;
    EnumExam(PayType payType){
        this.payType = payType;
    }
    private enum PayType{
        WEEKDAY, WEEKEND;
    }
}
