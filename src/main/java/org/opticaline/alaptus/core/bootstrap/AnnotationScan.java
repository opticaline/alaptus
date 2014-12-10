package org.opticaline.alaptus.core.bootstrap;

import org.opticaline.alaptus.core.ApplicationContext;
import org.opticaline.alaptus.core.ContextFactory;
import org.opticaline.alaptus.core.annotation.ParamTransaction;
import org.opticaline.alaptus.core.annotation.Route;
import org.opticaline.alaptus.core.bootstrap.*;
import org.opticaline.alaptus.core.bootstrap.AnonymousLoader;
import org.opticaline.alaptus.core.exception.SetupException;
import org.opticaline.alaptus.core.route.RouteContext;
import org.opticaline.alaptus.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nathan on 14-8-25.
 */
public class AnnotationScan {
    private static final Logger logger = LoggerFactory.getLogger(org.opticaline.alaptus.core.bootstrap.AnnotationScan.class);

    private RouteContext routeContext;
    private ApplicationContext context;
    private Map<Class, org.opticaline.alaptus.core.bootstrap.AnonymousLoader> loaders;

    private AnnotationScan() {
        routeContext = ContextFactory.getRouteContext();
        context = ContextFactory.getApplicationContext();
        loaders = new HashMap<>();
        loaders.put(ParamTransaction.class, new org.opticaline.alaptus.core.bootstrap.AnonymousLoader.ParamTransactionLoader(context));
        loaders.put(Route.class, new org.opticaline.alaptus.core.bootstrap.AnonymousLoader.RouteLoader(routeContext));
    }

    private org.opticaline.alaptus.core.bootstrap.AnnotationScan taken() throws URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class[] classes = ClassUtils.getProjectClassesPath();
        for (int i = 0; i < classes.length; i++) {
            Annotation[] classAnnotations = classes[i].getDeclaredAnnotations();
            for (int j = 0; j < classAnnotations.length; j++) {
                org.opticaline.alaptus.core.bootstrap.AnonymousLoader loader = this.loaders.get(classAnnotations[j].annotationType());
                if (loader != null) {
                    loader.load(classes[i], classAnnotations[j]);
                }
            }
            Method[] methods = classes[i].getDeclaredMethods();
            for (int j = 0; j < methods.length; j++) {
                Annotation[] annotations = methods[j].getDeclaredAnnotations();
                for (int k = 0; k < annotations.length; k++) {
                    AnonymousLoader loader = this.loaders.get(annotations[k].annotationType());
                    if (loader != null) {
                        loader.load(classes[i], methods[j], annotations[k]);
                    }
                }
            }
        }
        return this;
    }

    public static void scan() throws SetupException {
        try {
            new org.opticaline.alaptus.core.bootstrap.AnnotationScan().taken();
        } catch (URISyntaxException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new SetupException();
        }
    }
}
