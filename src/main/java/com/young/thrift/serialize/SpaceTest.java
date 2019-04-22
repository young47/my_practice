package com.young.thrift.serialize;

import com.alibaba.fastjson.JSON;
import com.young.thrift.module.Push;
import com.young.thrift.module.Push1;
import com.young.thrift.module.Push2;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;

import java.io.ByteArrayOutputStream;

public class SpaceTest {
    private static int num = 1;
    public static void main(String[] args) throws TException {
        Push2 push2 = new Push2();
        push2.setCid("6438994208421752841");
        push2.setOid("324058455");

        Push1 push1 = new Push1();
        push1.setCid(6438994208421752841l);
        push1.setOid(324058455);
        /*push.setMid("");
595be71e07c6b000 1350bd57
        UserInfo userInfo = new UserInfo();
        userInfo.setImei("12367898765678");
        userInfo.setToken(1234325234);
        userInfo.setPt("723481df5c045473c5a24d2cdb5e8ec1fca2e0b1");
        userInfo.setDt("9e2d08a1e1bca8dabfcd391fed2b97e52ebaae952e5145a72c8515db12542d91");
        userInfo.setPm(0);
        userInfo.setTm(25479915);
        userInfo.setCh("1020");
        userInfo.setCid("6438994208421752841");

        push.setUserInfo(userInfo);*/

        /*for (int i = 0; i < num; i++) {
            push.setCid("6438994208421752841");
            push.setOid("324058455");
        }*/

        tBinaryProtocolTest(push2);
        //tBinaryProtocolTest(push1);

        tCompactProtocol(push2);

        //common(push);
    }

    private static void common(Push push) {
        ByteArrayOutputStream baos;
        byte[] array;
        baos = new ByteArrayOutputStream();
        long begin2 = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            byte[] bytes = JSON.toJSONString(push).getBytes();
            baos.write(bytes, 0, bytes.length);
        }
        long end2 = System.currentTimeMillis();
        System.out.println(baos.toByteArray().length);
        System.out.println("fastjson seralize cost:" + (end2 - begin2) + "ms");
        array = baos.toByteArray();
        for (int i = 0; i < array.length; i++) {
            System.out.print(toHexString(array[i])+",");
        }
        System.out.println(" ");
    }

    private static void tCompactProtocol(TBase push) throws TException {
        ByteArrayOutputStream baos;
        byte[] array;
        TSerializer tSerializer1 = new TSerializer(new TCompactProtocol.Factory());
        baos = new ByteArrayOutputStream();
        //userInfo.setToken(1);
        long begin1 = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            byte[] bytes = tSerializer1.serialize(push);
            baos.write(bytes, 0, bytes.length);
        }
        long end1 = System.currentTimeMillis();
        System.out.println(baos.toByteArray().length);
        System.out.println("thrift compact seralize cost:" + (end1 - begin1) + "ms");
        array = baos.toByteArray();
        for (int i = 0; i < array.length; i++) {
            System.out.print(toHexString(array[i])+",");
        }
        System.out.println(" ");
        System.out.println(" ");
    }

    private static void tBinaryProtocolTest(TBase push) throws TException {
        TSerializer tSerializer = new TSerializer(new TBinaryProtocol.Factory());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            byte[] bytes = tSerializer.serialize(push);
            baos.write(bytes, 0, bytes.length);
        }
        long end = System.currentTimeMillis();
        System.out.println(baos.toByteArray().length);
        System.out.println("thrift binary seralize cost:" + (end - begin) + "ms");
        byte[] array = baos.toByteArray();
        for (int i = 0; i < array.length; i++) {
            System.out.print(toHexString(array[i])+",");
        }
        System.out.println(" ");
        System.out.println(" ");
    }

    private static String toHexString(byte b){
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1){
            return "0" + s;
        }else{
            return s;
        }
    }

}
