package com.young.thrift.serialize;

import com.alibaba.fastjson.JSON;
import com.young.thrift.module.Push;
import com.young.thrift.module.UserInfo;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;

import java.io.ByteArrayOutputStream;

public class SpaceTest {
    private static int num = 10000;
    public static void main(String[] args) throws TException {
        Push push = new Push();
        push.setCid("6438994208421752841");
        push.setOid("324058455");
        push.setMid("381815491");

        UserInfo userInfo = new UserInfo();
        userInfo.setImei("123");
        userInfo.setToken(1234325234);
        userInfo.setPt("ABC");

        push.setUserInfo(userInfo);

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
        /*byte[] array = baos.toByteArray();
        for (int i = 0; i < array.length; i++) {
            System.out.print(toHexString(array[i])+",");
        }
        System.out.println(" ");
        System.out.println(" ");*/

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
       /* array = baos.toByteArray();
        for (int i = 0; i < array.length; i++) {
            System.out.print(toHexString(array[i])+",");
        }
        System.out.println(" ");
        System.out.println(" ");*/

        baos = new ByteArrayOutputStream();
        long begin2 = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            byte[] bytes = JSON.toJSONString(push).getBytes();
            baos.write(bytes, 0, bytes.length);
        }
        long end2 = System.currentTimeMillis();
        System.out.println(baos.toByteArray().length);
        System.out.println("fastjson seralize cost:" + (end2 - begin2) + "ms");
       /* array = baos.toByteArray();
        for (int i = 0; i < array.length; i++) {
            System.out.print(toHexString(array[i])+",");
        }
        System.out.println(" ");*/
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
