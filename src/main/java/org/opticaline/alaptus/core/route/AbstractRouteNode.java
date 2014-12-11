package org.opticaline.alaptus.core.route;

import java.util.Map;

/**
 * Created by Nathan on 14-8-25.
 */
public abstract class AbstractRouteNode implements org.opticaline.alaptus.core.route.RouteNode {
    protected MethodMap self;
    protected Map<String, AbstractRouteNode> routeNodeMap;

    @Override
    public void setSelf(org.opticaline.alaptus.core.route.RouteBean method, org.opticaline.alaptus.core.config.HttpMethod httpMethod) {
        this.self.put(httpMethod, method);
    }

    @Override
    public org.opticaline.alaptus.core.route.RouteBean getSelf(org.opticaline.alaptus.core.config.HttpMethod httpMethod) {
        return this.self.get(httpMethod);
    }

    @Override
    public org.opticaline.alaptus.core.route.RouteBean[] getAll() {
        return (org.opticaline.alaptus.core.route.RouteBean[]) routeNodeMap.values().toArray();
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
