package com.young;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonSpeedTest {
    public static void main(String[] args) {
        int cnt = 100000;
        jsonTest(cnt);
        //sbTest(cnt);
        //mapTest(cnt);

    }

    private static void mapTest(int cnt) {
        long l = System.currentTimeMillis();
        Map map = new HashMap(16);
        for (int i = 0; i < cnt; i++) {

            map.put("platform", "android");

            map.put("factory", "huawei");

            map.put("pid", 1234567898765432l);

            map.put("messageId", 123456543);

            map.put("newsId", 234587654);

            map.put("msgType", "2345jh");

            map.put("ts", System.currentTimeMillis() / 1000);

            map.put("cid", "6409303157587226649");

            map.put("token", "37d3c6d90a3152f9a22b01b03a2a7d99538e4f1c");

            map.put("src", "37d3c6d90a3152f9a22b01b03a2a7d99538e4f1c");

            map.put("flag", 1);

            map.put("brokerIp", "127.0.0.1");

            map.put("rs", "hahahahahahahhaha");
            //map.toString();
            //System.out.println(map.toString());
            map.clear();
        }
        System.out.println(cnt + " map cost:" + (System.currentTimeMillis() - l) + "ms");
    }

    private static void sbTest(int cnt) {
        long l = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            StringBuilder traceSb = new StringBuilder();
            traceSb.append("{");
            traceSb.append("\"platform\":" + "\"android\",");
            //traceSb.append("\"factory\":" + "huawei" + ",");
            traceSb.append("factory").append(":").append("huawei").append(",");
            traceSb.append("\"pid\":" + 1234567898765432l + ",");
            traceSb.append("\"messageId\":" + 123456543 + ",");
            traceSb.append("\"newsId\":\"" + 234587654 + "\",");
            traceSb.append("\"msgType\":\"" + "2345jh" + "\",");
            traceSb.append("\"ts\":" + System.currentTimeMillis() / 1000 + ",");
            traceSb.append("\"cid\":\"" + "6409303157587226649" + "\",");
            traceSb.append("\"token\":\"" + "37d3c6d90a3152f9a22b01b03a2a7d99538e4f1c" + "\",");
            traceSb.append("\"src\":\"" + "37d3c6d90a3152f9a22b01b03a2a7d99538e4f1c" + "\",");
            traceSb.append("\"source\":0,");
            traceSb.append("\"brokerIp\":\"" + "127.0.0.1" + "\",");
            traceSb.append("\"rs\":" + "hahahahahahahhaha");
            traceSb.append("}");
            //traceSb.toString();
            //System.out.println(traceSb.toString());

        }
        System.out.println(cnt + " stringBuilder cost:" + (System.currentTimeMillis() - l) + "ms");
    }

    private static void jsonTest(int cnt) {
        long l = System.currentTimeMillis();
        JSONObject trace_json = new JSONObject(16);
        for (int i = 0; i < cnt; i++) {

            trace_json.put("platform", "android");

            trace_json.put("factory", "huawei");

            trace_json.put("pid", 1234567898765432l);

            trace_json.put("messageId", 123456543);

            trace_json.put("newsId", 234587654);

            trace_json.put("msgType", "2345jh");

            trace_json.put("ts", System.currentTimeMillis() / 1000);

            trace_json.put("cid", "6409303157587226649");

            trace_json.put("token", "37d3c6d90a3152f9a22b01b03a2a7d99538e4f1c");

            trace_json.put("src", "37d3c6d90a3152f9a22b01b03a2a7d99538e4f1c");

            trace_json.put("flag", 1);

            trace_json.put("brokerIp", "127.0.0.1");

            trace_json.put("rs", "hahahahahahahhaha");
            trace_json.toJSONString();
            //System.out.println(trace_json.toJSONString());
            trace_json.clear();
        }
        System.out.println(cnt + " json cost:" + (System.currentTimeMillis() - l) + "ms");

    }
}
