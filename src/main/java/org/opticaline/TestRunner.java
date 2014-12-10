package org.opticaline;

import java.net.URL;

/**
 * Created by Nathan on 2014/12/5.
 */
public class TestRunner {

    @TestParam
    public String test = "hello";

    @TestParam
    private String test2 = "world";

    public static void main(String[] args) throws IllegalAccessException {
        System.out.println(TestRunner.class.getDeclaredFields()[0].getDeclaredAnnotations()[0]);
    }
}
