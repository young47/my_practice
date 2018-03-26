package com.young.regex;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by young on 16/12/22.
 */

public class RegexTest {
    public static void main(String[] args){
        Pattern pattern = Pattern.compile("\\d{3,5}");
        String charSequence = "123-34345-234-00";
        Matcher matcher = pattern.matcher(charSequence);
        System.out.println(System.getProperty("jdbc.drivers"));
        //虽然匹配失败，但由于charSequence里面的"123"和pattern是匹配的,所以下次的匹配从位置4开始
        System.out.println("matches: "+matcher.matches());   //false

        //测试匹配位置
        System.out.println("第一次 find： "+matcher.find());
        System.out.println(matcher.start());  // 4

        //使用reset方法重置匹配位置
        matcher.reset();

        //第一次find匹配以及匹配的目标和匹配的起始位置
        System.out.println("第二次 find： "+matcher.find());   // 0
        System.out.println(matcher.group()+" - "+matcher.start());

        //第二次find匹配以及匹配的目标和匹配的起始位置
        System.out.println("第三次 find： "+matcher.find());  //4
        System.out.println(matcher.group()+" - "+matcher.start());

        //第一次lookingAt匹配以及匹配的目标和匹配的起始位置
        // System.out.println("5:");
        // System.out.println(matcher.lookingAt()); //0
        // System.out.println(matcher.group()+" - "+matcher.start());

        //第二次lookingAt匹配以及匹配的目标和匹配的起始位置
        System.out.println("6:");
        System.out.println(matcher.lookingAt()); //0
        System.out.println(matcher.group()+" - "+matcher.start());
    }

}