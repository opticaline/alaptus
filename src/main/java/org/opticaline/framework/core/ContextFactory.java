package org.opticaline.framework.core;

import org.opticaline.framework.core.route.RouteContext;

/**
 * Created by Nathan on 14-8-25.
 */
public class ContextFactory {
    private static ApplicationContext applicationContext = null;
    private static RouteContext routeContext = null;

    private ContextFactory() {
    }

    static {
        applicationContext = new ApplicationContext();
        routeContext = new RouteContext();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static RouteContext getRouteContext() {
        return routeContext;
    }
}
