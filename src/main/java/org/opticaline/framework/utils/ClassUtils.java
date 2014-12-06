package org.opticaline.framework.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Nathan on 14-8-26.
 */
public class ClassUtils {
    private static final Log logger = LogFactory.getLog(ClassUtils.class);
/*
    public static List<File> scanFiles(String path) {
        System.out.println(path);
        return scanFiles(new File(path));
    }*/

    public static List<File> scanFiles(File dir) {
        File[] files = dir.listFiles();
        List<File> temp = new ArrayList<File>();
        for (File file : files != null ? files : new File[0]) {
            if (file.isDirectory()) {
                temp.addAll(scanFiles(file));
            } else {
                if (file.getName().endsWith(".class"))
                    temp.add(file);
            }
        }
        return temp;
    }

    public static List<File> scanFiles(URI uri) {
        return scanFiles(new File(uri));
    }

    public static List<File> scanFiles(String path) {
        return scanFiles(new File(path));
    }

    public static Class[] getClassPath(List<File> files) throws URISyntaxException, ClassNotFoundException {
        int length = ClassUtils.class.getResource("/").toURI().getPath().substring(1).replaceAll("/", "\\\\").length();
        int size = files.size();
        ClassLoader classLoader = ClassUtils.class.getClassLoader();
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
        return getClassPath(scanFiles(ClassUtils.class.getResource("/").toURI()));
    }

    public static List<String> getPaths() {
        String[] jarPaths = System.getProperty("java.class.path").split(";");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, jarPaths);
        return list;
    }

    public static Set<Class> getAllClasses() throws URISyntaxException, ClassNotFoundException, IOException {
        List<String> path = getPaths();
        Set<Class> set = new HashSet<>();
        boolean btn = false;
        for (String p : path) {
            if (p.toLowerCase().endsWith(".jar")) {
                if (btn) {
                    Collections.addAll(set, getClassesForJar(new JarFile(p)));
                }
            } else {
                Collections.addAll(set, getClassPath(scanFiles(p)));
                btn = true;
            }
        }
        return set;
    }

    public static Class[] getClassesForJar(JarFile jar) {
        Enumeration<JarEntry> entries = jar.entries();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        List<Class> list = new ArrayList<>();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.endsWith(".class")) {
                name = name.replaceAll("\\.class$", "").replaceAll("/", ".");
                try {
                    list.add(loader.loadClass(name));
                } catch (Exception | NoClassDefFoundError e) {
                    logger.info(name);
                }
            }
        }
        return list.toArray(new Class[list.size()]);
    }

    public static void main(String[] args) throws URISyntaxException, ClassNotFoundException, IOException {
        /*Enumeration name = System.getProperties().propertyNames();
        while (name.hasMoreElements()) {
            String n = name.nextElement().toString();
            System.out.println(n);
            System.out.println(System.getProperty(n));
        }*/
        System.out.println(getAllClasses());
    }
}
