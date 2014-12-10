package org.opticaline.alaptus.core.loader;

import org.apache.commons.lang3.ArrayUtils;
import org.opticaline.alaptus.utils.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Nathan on 2014/12/5.
 */
public class LoaderController {
    private Set<Class> all;

    public LoaderController(String[] pkg) {
        init(pkg);
    }

    private void init(String[] pkg) {
        try {
            all = ClassUtils.getClasses(pkg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        load(all);
    }

    /**
     * @param pgPath
     */
    public void load(String pgPath) {
        try {
            load(ClassUtils.getClasses(new String[]{pgPath}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load(Set<Class> classes) {
        Set<AbstractLoadStandards> standards = new LinkedHashSet<>();
        classes.stream()
                .filter(clazz -> AbstractLoadStandards.class.isAssignableFrom(clazz)
                        && !Modifier.isAbstract(clazz.getModifiers()))
                .forEach(clazz -> {
                    try {
                        standards.add((AbstractLoadStandards) clazz.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        classes.stream()
                .filter(clazz -> {
                    for (AbstractLoadStandards standard : standards) {
                        TypeVariable[] variables = standard.getClass().getTypeParameters();
                        if (variables.length > 0) {
                            System.out.println(variables[0]);
                        }
                        if (ArrayUtils.contains(clazz.getAnnotations(), standard.atParameter)) {
                            return true;
                        }
                    }
                    return false;
                })
                .forEach(clazz -> {
                    System.out.println(standards.size() + "+++");
                    for (AbstractLoadStandards standard : standards) {
                        System.out.println(standard.getClass().getTypeParameters());
                      /*  if(ArrayUtils.contains(standard.atParameter, )){

                        }*/
                    }
                });
    }
}
