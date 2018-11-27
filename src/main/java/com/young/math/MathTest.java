package com.young.math;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.List;
import java.util.Objects;

public class MathTest {

    public static void main(String[] args) throws Exception {
        System.out.println(Math.round(11.5));
        System.out.println(Math.round(-11.5));
        System.out.println(Math.round(11.4));
        System.out.println(Math.round(-11.4));
        byte s = 'a';
        switch (s) {
            case 'c':
                break;

        }

        String key = "ab";
        switch (key) {
            case "s":
            case "f":
                System.out.println(key);
        }

        BufferedReader br = new BufferedReader(new FileReader("/Users/young/Desktop/text.txt"));
        String line = null;
        int total = 0;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("#")) {
                continue;
            }
            total++;
            final List<String> stringList = Splitter.on("\t").trimResults().omitEmptyStrings().splitToList(line);
            final String cid = stringList.get(0).trim();
            if (stringList.size() > 1) {
                final String jsonStr = stringList.get(1);
                final JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                if (Objects.nonNull(jsonObject)) {
                    String version = StringUtils.isBlank(jsonObject.getString("v")) ? "" : jsonObject.getString("v");
                    System.out.println(cid + "-" + version);
                }
            }

        }
        System.out.println("total=" + total);


    }
}
