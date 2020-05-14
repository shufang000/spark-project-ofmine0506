package com.shufang.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestJavaProperties {

     Properties properties = new Properties();

    public static void main(String[] args) throws IOException {

        TestJavaProperties testJavaProperties = new TestJavaProperties();

        InputStream stream = testJavaProperties.getClass().getClassLoader().getResourceAsStream("src/main/resources/jdbc.properties");

        Properties properties = testJavaProperties.properties;
        properties.load(stream);

        System.out.println(properties.getProperty("url"));
        System.out.println(properties.getProperty("user"));
        System.out.println(properties.getProperty("password"));
        System.out.println(properties.getProperty("driver"));
        System.out.println(properties.getProperty("dbtable"));
    }
}
