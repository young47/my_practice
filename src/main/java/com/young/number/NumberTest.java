package com.young.number;

/**
 * Created by young on 18/1/3.
 */
public class NumberTest {
    private static float f_num;
    private static double d_num;
    public static void main(String[] args) {
      /*  float f = 1.23f;
        System.out.println(f);
        //System.out.println(0.03f+0.010000064300f);
        //System.out.println(123.3/100);
        //System.out.println(4.015*100);
        //System.out.println(f_num);
        System.out.println(d_num);*/

        Integer a = 500; //这是装箱 valueOf()
        int b = 500;
        Integer c = 500;
        System.out.println(a==b); //这里拆箱 intValue()
        System.out.println(b==a); //这里拆箱 intValue()
        System.out.println(c==a); //false

        long totalCnt = 728747;

        int thisProgress = (int) (100 * 61178 / totalCnt);
        System.out.println(thisProgress);

        int sum = 60808+60865+61178+60671+60772+60851+60095+60644+60686+60935+60268+60974;
        System.out.println("sum="+sum);



    }
}
