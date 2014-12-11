package org.opticaline.alaptus.core.loader;

import org.opticaline.alaptus.core.annotation.Plugin;
import org.opticaline.alaptus.utils.ClassUtils;

import java.io.IOException;
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
        classes.stream()
                .filter(clazz -> clazz.getDeclaredAnnotation(Plugin.class) != null)
                .forEach(clazz -> new PluginHandler(clazz).handler());
    }

    public Set<Class> getClasses() {
        return all;
    }


}
