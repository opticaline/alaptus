package org.opticaline.framework.corebak.route;

import org.opticaline.framework.corebak.config.HttpMethod;

import java.util.HashMap;

/**
 * Created by Nathan on 14-8-25.
 */
public interface RouteNode {

    public void setSelf(RouteBean method, HttpMethod httpMethod);

    public RouteBean getSelf(HttpMethod httpMethod);

    public RouteBean[] getAll();

    public String pathPretreatment(String string) throws Exception;

    public void addRoute(String path, HttpMethod httpMethod, RouteBean method);

    public void addRoute(String[] paths, HttpMethod httpMethod, RouteBean method);

    public RouteBean getRoute(String path, HttpMethod httpMethod);

    public RouteBean getRoute(String[] paths, HttpMethod httpMethod);

    class MethodMap extends HashMap<HttpMethod, RouteBean> {
    }
}
