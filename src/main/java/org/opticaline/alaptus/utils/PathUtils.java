package org.opticaline.alaptus.utils;

/**
 * Created by Nathan on 14-8-25.
 */
public class PathUtils {
    public static String settingPath2Real(String path) {
        if (path.toUpperCase().startsWith("CLASSPATH:")) {
            path = PathUtils.class.getResource("/") + path.substring(10);
        } else if (path.toUpperCase().startsWith("WEB-INF:")) {
            path = PathUtils.class.getResource("/") + path.substring(8);
        }
        return path;
    }

    public static void main(String[] args) {
        System.out.println(settingPath2Real("WEB-INF:application.xml"));
    }
}
