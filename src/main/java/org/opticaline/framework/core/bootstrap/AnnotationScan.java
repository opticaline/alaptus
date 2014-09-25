package org.opticaline.framework.core.bootstrap;

import org.opticaline.framework.core.ApplicationContext;
import org.opticaline.framework.core.ContextFactory;
import org.opticaline.framework.core.annotation.Param;
import org.opticaline.framework.core.annotation.ParamTransaction;
import org.opticaline.framework.core.annotation.Route;
import org.opticaline.framework.core.exception.SetupException;
import org.opticaline.framework.core.format.Transaction;
import org.opticaline.framework.core.route.RouteBean;
import org.opticaline.framework.core.route.RouteContext;
import org.opticaline.framework.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.opticaline.framework.core.bootstrap.AnonymousLoader.*;

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
        Class[] classes = FileUtils.getProjectClassesPath();
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
