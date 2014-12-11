package org.opticaline.alaptus.core.route;

import java.util.HashMap;

/**
 * Created by Nathan on 14-8-25.
 */
public interface RouteNode {

    public void setSelf(org.opticaline.alaptus.core.route.RouteBean method, org.opticaline.alaptus.core.config.HttpMethod httpMethod);

    public org.opticaline.alaptus.core.route.RouteBean getSelf(org.opticaline.alaptus.core.config.HttpMethod httpMethod);

    public org.opticaline.alaptus.core.route.RouteBean[] getAll();

    public String pathPretreatment(String string) throws Exception;

    public void addRoute(String path, org.opticaline.alaptus.core.config.HttpMethod httpMethod, org.opticaline.alaptus.core.route.RouteBean method);

    public void addRoute(String[] paths, org.opticaline.alaptus.core.config.HttpMethod httpMethod, org.opticaline.alaptus.core.route.RouteBean method);

    public org.opticaline.alaptus.core.route.RouteBean getRoute(String path, org.opticaline.alaptus.core.config.HttpMethod httpMethod);

    public org.opticaline.alaptus.core.route.RouteBean getRoute(String[] paths, org.opticaline.alaptus.core.config.HttpMethod httpMethod);

    class MethodMap extends HashMap<org.opticaline.alaptus.core.config.HttpMethod, org.opticaline.alaptus.core.route.RouteBean> {
    }
}
