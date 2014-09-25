package org.opticaline.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 14-8-26.
 */
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
/*
    public static List<File> scanFiles(String path) {
        System.out.println(path);
        return scanFiles(new File(path));
    }*/

    public static List<File> scanFiles(File dir) {
        File[] files = dir.listFiles();
        int length = files.length;
        List<File> temp = new ArrayList<File>();
        for (int i = 0; i < length; i++) {
            if (files[i].isDirectory()) {
                temp.addAll(scanFiles(files[i]));
            } else {
                if (files[i].getName().endsWith(".class"))
                    temp.add(files[i]);
            }
        }
        return temp;
    }

    public static List<File> scanFiles(URI uri) {
        return scanFiles(new File(uri));
    }

    public static Class[] getClassPath(List<File> files) throws URISyntaxException, ClassNotFoundException {
        int length = FileUtils.class.getResource("/").toURI().getPath().substring(1).replaceAll("/", "\\\\").length();
        int size = files.size();
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        Class[] classes = new Class[size];
        for (int i = 0; i < size; i++) {
            String t = files.get(i).getPath().substring(length);
            int leftLength = t.length() - 6;
            t = t.substring(0, leftLength).replaceAll("\\\\", "\\.");
            classes[i] = classLoader.loadClass(t);
        }
        return classes;
    }

    public static Class[] getProjectClassesPath() throws URISyntaxException, ClassNotFoundException {
        return getClassPath(scanFiles(FileUtils.class.getResource("/").toURI()));
    }

    public static void main(String[] args) throws URISyntaxException, ClassNotFoundException {
        for (Class file : getProjectClassesPath()) {
            System.out.println(file);
        }
    }
}
