package org.opticaline.framework.corebak.route;

import org.opticaline.framework.corebak.config.HttpMethod;

import java.util.Map;

/**
 * Created by Nathan on 14-8-25.
 */
public abstract class AbstractRouteNode implements RouteNode {
    protected MethodMap self;
    protected Map<String, AbstractRouteNode> routeNodeMap;

    @Override
    public void setSelf(RouteBean method, HttpMethod httpMethod) {
        this.self.put(httpMethod, method);
    }

    @Override
    public RouteBean getSelf(HttpMethod httpMethod) {
        return this.self.get(httpMethod);
    }

    @Override
    public RouteBean[] getAll() {
        return (RouteBean[]) routeNodeMap.values().toArray();
    }

    protected String[] takenPath(String path) {
        String[] temps = path.split("/");
        if (temps.length == 0) {
            temps = new String[]{"/"};
        } else {
            temps[0] = "/";
        }
        return temps;
    }
}
