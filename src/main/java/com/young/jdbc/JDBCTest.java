package com.young.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by young on 16/12/22.
 */
public class JDBCTest {
    private static String url = "jdbc:mysql://localhost:3306/etl";
    private static int port = 3306;
    private static String userName = "root";
    private static String pwd = "root";

    private static String driver = "com.mysql.jdbc.Driver";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        template();
        //test();


    }

    private static void test() {
        /*try {
            java.sql.Driver driver = new com.mysql.jdbc.Driver();
            Properties info = new Properties();
            info.put("user", userName);
            info.put("password", pwd);
            Connection conn = driver.connect(url, info);
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("select * from etl_edit");
            while (res.next()){
                int id = res.getInt("id");
                System.out.println(id);
            }
            res.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    private static void template() throws ClassNotFoundException, SQLException {
        //Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        //System.out.println(aClass.getClassLoader());
        System.out.println(System.getProperty("jdbc.drivers"));
        Connection conn = DriverManager.getConnection(url, userName, pwd);

        Statement stat = conn.createStatement();

        ResultSet res = stat.executeQuery("select * from etl_edit");

        while (res.next()){
            int id = res.getInt("id");
            System.out.println(id);
        }
        res.close();
        conn.close();

    }
}
