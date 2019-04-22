package com.young;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Temp1 {
    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
        //String s = new String("1");
        System.out.println(System.currentTimeMillis() & 1);
        String[] split = "340340520\t84".split("\t");
        System.out.println(split[0]+":"+split[1]);
        /*try {
            DriverManager.getConnection("localhost");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        /*System.out.println("https://qf.56.com/56".hashCode());
        System.out.println("https://qf.56.com/56509".hashCode());
        System.out.println("https://qf.56.com/56508".hashCode());
        System.out.println("https://qf.56.com/156508".hashCode());
        System.out.println("https://qf.56.com/1234556508".hashCode());*/
       /* String url = "https://qf.56.com/";
        Random random = new Random(10000000);
        for (int j = 0; j < 100; j++) {
            String s = url + Math.abs(random.nextInt(100000));
            String hashCode = Math.abs(s.hashCode()) + "";
            System.out.println(s+" == "+ StringUtils.leftPad(hashCode, 10, "19"));
        }*/
        /*try {
            FileWriter fileWriter = new FileWriter("/tmp/bloomkey-space-test.txt");
            BufferedWriter bw = new BufferedWriter(fileWriter);
            String key = "6433219942484979839,349593649";
            int total = 1000_0000,i=0;

            while (i++<total){
                bw.write(key);
                bw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        float i = (float)5 / 11;
        System.out.println("i="+new java.text.DecimalFormat("#.##").format(i));
        System.out.println(120000 % 10000 == 0);

        int a = 1195725856;
        int b = 1937006964;
        int c = 1096111176;
        int d = 369295872;
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putInt(a).putInt(b).putInt(c).putInt(d);
        buffer.flip();
        byte[] bytes = new byte[16];
        buffer.get(bytes);
        System.out.println(new String(bytes));
    }
}
