package org.opticaline;

import java.net.URL;

/**
 * Created by Nathan on 2014/12/5.
 */
public class TestRunner {

    public static void main(String[] args) {
        String packageName = "";
        URL url = Thread.currentThread().getContextClassLoader().getResource(packageName);
        if (url != null) {
            System.out.println(url.getPath());
        }
    }
}
