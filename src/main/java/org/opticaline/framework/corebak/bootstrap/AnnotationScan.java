package org.opticaline.framework.corebak.bootstrap;

import org.opticaline.framework.corebak.ApplicationContext;
import org.opticaline.framework.corebak.ContextFactory;
import org.opticaline.framework.corebak.annotation.ParamTransaction;
import org.opticaline.framework.corebak.annotation.Route;
import org.opticaline.framework.corebak.exception.SetupException;
import org.opticaline.framework.corebak.route.RouteContext;
import org.opticaline.framework.utils.ClassUtils;
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
    private static final Logger logger = LoggerFactory.getLogger(AnnotationScan.class);

    private RouteContext routeContext;
    private ApplicationContext context;
    private Map<Class, AnonymousLoader> loaders;

    private AnnotationScan() {
        routeContext = ContextFactory.getRouteContext();
        context = ContextFactory.getApplicationContext();
        loaders = new HashMap<>();
        loaders.put(ParamTransaction.class, new AnonymousLoader.ParamTransactionLoader(context));
        loaders.put(Route.class, new AnonymousLoader.RouteLoader(routeContext));
    }

    private AnnotationScan taken() throws URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class[] classes = ClassUtils.getProjectClassesPath();
        for (int i = 0; i < classes.length; i++) {
            Annotation[] classAnnotations = classes[i].getDeclaredAnnotations();
            for (int j = 0; j < classAnnotations.length; j++) {
                AnonymousLoader loader = this.loaders.get(classAnnotations[j].annotationType());
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
            new AnnotationScan().taken();
        } catch (URISyntaxException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new SetupException();
        }
    }
}
