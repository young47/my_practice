package com.young.shift;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 17/9/19.
 */
public class MoveOper {

    public static void main(String[] args){
        Integer i = -7;
        System.out.println(i+" byte :   "+i.byteValue());
        System.out.println(i+" binary : " + Integer.toBinaryString(i)+" : "+i);
        System.out.println(i+" >> 1 :   "+ Integer.toBinaryString(i>>1)+" : "+String.valueOf(i>>1));
        System.out.println(i+" >> 2 :   "+ Integer.toBinaryString(i>>2)+" : "+String.valueOf(i>>2));
        System.out.println(i+" >>> 1 :  "+ Integer.toBinaryString(i>>>1)+" : "+String.valueOf(i>>>1));
        System.out.println(i+" >>> 2 :  "+ Integer.toBinaryString(i>>>2)+" : "+String.valueOf(i>>>2));

        i = Integer.MAX_VALUE;
        System.out.println(i+" byte :   "+i.byteValue());
        System.out.println(i+" binary : " + Integer.toBinaryString(i)+" : "+i);
        System.out.println(i+" >> 1 :   "+ Integer.toBinaryString(i>>1)+" : "+String.valueOf(i>>1));
        System.out.println(i+" >> 2 :   "+ Integer.toBinaryString(i>>2)+" : "+String.valueOf(i>>2));
        System.out.println(i+" >>> 1 :  "+ Integer.toBinaryString(i>>>1)+" : "+String.valueOf(i>>>1));
        System.out.println(i+" >>> 2 :  "+ Integer.toBinaryString(i>>>2)+" : "+String.valueOf(i>>>2));


        i = Integer.MIN_VALUE;
        System.out.println(i+" byte :   "+i.byteValue());
        System.out.println(i+" binary : " + Integer.toBinaryString(i)+" : "+i);
        System.out.println(i+" >> 1 :   "+ Integer.toBinaryString(i>>1)+" : "+String.valueOf(i>>1));
        System.out.println(i+" >> 2 :   "+ Integer.toBinaryString(i>>2)+" : "+String.valueOf(i>>2));
        System.out.println(i+" >>> 1 :  "+ Integer.toBinaryString(i>>>1)+" : "+String.valueOf(i>>>1));
        System.out.println(i+" >>> 2 :  "+ Integer.toBinaryString(i>>>2)+" : "+String.valueOf(i>>>2));


        Byte b = Byte.MAX_VALUE;
        System.out.println(b+" byte :   "+b.byteValue());
        System.out.println(b+" binary :   "+ Integer.toBinaryString(b & 0xFF));
        System.out.println(b+" >> 1 :   "+ Integer.toBinaryString(b>>1 & 0xFF & 0xFF));
        System.out.println(b+" >> 2 :   "+ Integer.toBinaryString(b>>2 & 0xFF)+" : "+String.valueOf(b>>2 & 0xFF));
        System.out.println(b+" >>> 1 :  "+ Integer.toBinaryString(b>>>1 & 0xFF)+" : "+String.valueOf(b>>>1 & 0xFF));
        System.out.println(b+" >>> 2 :  "+ Integer.toBinaryString(b>>>2 & 0xFF)+" : "+String.valueOf(b>>>2 & 0xFF));

        b = Byte.MIN_VALUE;
        System.out.println(b+" byte :   "+b.byteValue());
        System.out.println(b+" binary :   "+ Integer.toBinaryString(b & 0xFF));
        System.out.println(b+" >> 1 :   "+ Integer.toBinaryString(b>>1 & 0xFF)+" : "+String.valueOf(b>>1));
        System.out.println(b+" >> 2 :   "+ Integer.toBinaryString(b>>2 & 0xFF)+" : "+String.valueOf(b>>2));
        System.out.println(b+" >>> 1 :  "+ Integer.toBinaryString(b>>>1)+" : "+String.valueOf(b>>>1 & 0xFF));
        System.out.println(b+" >>> 2 :  "+ Integer.toBinaryString(b>>>2)+" : "+String.valueOf(b>>>2 & 0xFF));
        /*System.out.println(i |= i >>> 2);
        System.out.println(i+" binary : " + Integer.toBinaryString(i)+" : "+i);


        System.out.println(i |= i >>> 4);
        System.out.println(i+" binary : " + Integer.toBinaryString(i)+" : "+i);


        System.out.println(i |= i >>> 8);
        System.out.println(i+" binary : " + Integer.toBinaryString(i)+" : "+i);


        System.out.println(i |= i >>> 16);
        System.out.println(i+" binary : " + Integer.toBinaryString(i)+" : "+i);*/

        Character c = '1';
        System.out.println(Integer.valueOf(String.valueOf(c)));

        String s = "123";
        System.out.println(Integer.parseInt(s));

        //9223 37203 68547 75809
        //9223372036854775807
        System.out.println(Long.MAX_VALUE);

        long ii = 123;
        System.out.println(-ii);

        char a = 'a', cc = 'b';
        System.out.println(a+cc);

        List<String> list = new ArrayList<String>();
        list.add("aa");
        list.add("bb");
        System.out.println(list);
        for (String s1 : list) {
             s1 = s1.concat("hh");
        }
        System.out.println(list);


    }

    @PostConstruct
    public void test(){
        System.out.println("@PostConstruct.....");
    }
}
