package org.opticaline.framework.core.loader;

import org.opticaline.framework.utils.ClassUtils;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Nathan on 2014/12/5.
 */
public class LoaderController {
    private Set<Class> all;

    public LoaderController(String[] pkg) {
        init(pkg);
        System.out.println(all.size());
    }

    private void init(String[] pkg) {
        try {
            all = ClassUtils.getClasses(pkg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        loadClass(all);
    }

    /**
     * @param pgPath
     */
    public void load(String pgPath) {

    }

    private void loadClass(Set<Class> classes) {
        for (Class clazz : classes) {

        }
    }

    //


}
