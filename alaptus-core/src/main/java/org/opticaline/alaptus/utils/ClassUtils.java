package org.opticaline.alaptus.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Nathan on 14-8-26.
 */
public class ClassUtils {
    private static final Log logger = LogFactory.getLog(ClassUtils.class);

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
        Enumeration name = System.getProperties().propertyNames();
        while (name.hasMoreElements()) {
            String n = name.nextElement().toString();
        }
        String[] jarPaths = System.getProperty("java.class.path").split(";");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, jarPaths);
        return list;
    }

    public static Set<Class> getClasses(String[] pkg) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Set<Class> set = new LinkedHashSet<>();
        for (String p : pkg) {
            Enumeration<URL> enumeration = classLoader.getResources(p.replace(".", "/"));
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                String protocol = url.getProtocol();
                if (protocol.equals("jar")) {
                    try {
                        Collections.addAll(set, getClassesForJar(((JarURLConnection) url.openConnection())
                                .getJarFile()));
                    } catch (ClassNotFoundException | NoClassDefFoundError e) {
                        logger.trace(p);
                    }
                } else {
                    try {
                        Collections.addAll(set, getClassPath(scanFiles(classLoader.getResource(p.replace(".", "/")).getPath())));
                    } catch (ClassNotFoundException | URISyntaxException e) {
                        logger.trace(p);
                    }
                }
            }
        }
        return set;
    }

    public static Class[] getClassesForJar(JarFile jar) throws ClassNotFoundException, NoClassDefFoundError {
        Enumeration<JarEntry> entries = jar.entries();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        List<Class> list = new ArrayList<>();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.endsWith(".class")) {
                name = name.replaceAll("\\.class$", "").replaceAll("/", ".");
                list.add(loader.loadClass(name));
            }
        }
        return list.toArray(new Class[list.size()]);
    }

    public static Method[] getMethodsByAnnotation(Class clazz, Class annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        Method[] result = new Method[0];
        for (Method method : methods) {
            if (method.getAnnotation(annotation) != null) {
                result = ArrayUtils.add(result, method);
            }
        }
        return result;
    }
}
