package org.opticaline.framework.core.loader;

import org.apache.commons.lang3.StringUtils;
import org.opticaline.framework.utils.ClassUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * Created by Nathan on 2014/12/5.
 */
public class LoaderController {
    private Set<Class> all;

    public LoaderController() {
        init();
        System.out.println(StringUtils.join(all, " ,"));
    }

    private void init() {
        try {
            all = ClassUtils.getAllClasses();
        } catch (URISyntaxException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {

    }

    /**
     * @param pgPath
     */
    public void load(String pgPath) {

    }

    //


}
