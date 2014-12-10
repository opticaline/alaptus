package org.opticaline.alaptus.core.route;

import org.apache.commons.lang3.ArrayUtils;
import org.opticaline.alaptus.core.Context;
import org.opticaline.alaptus.core.ContextFactory;
import org.opticaline.alaptus.core.config.HttpMethod;
import org.opticaline.alaptus.core.exception.IllegalPathException;
import org.opticaline.alaptus.core.route.*;
import org.opticaline.alaptus.core.route.RouteBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nathan on 14-8-25.
 */
public class RouteContext extends AbstractRouteNode implements Context {
    private static final Logger logger = LoggerFactory.getLogger(RouteContext.class);

    public RouteContext() {
        this.self = new MethodMap();
        this.routeNodeMap = new HashMap<>();
    }

    @Override
    public String pathPretreatment(String string) throws org.opticaline.alaptus.core.exception.IllegalPathException {
        return RoutePathRewrite.rewrite(string);
    }

    @Override
    public void addRoute(String path, org.opticaline.alaptus.core.config.HttpMethod httpMethod, org.opticaline.alaptus.core.route.RouteBean method) {
        String[] paths;
        if (path.equals("/")) {
            paths = new String[]{""};
        } else {
            paths = path.split("/");
        }
        if (paths.length >= 1 && paths[0].length() == 0) {
            this.addRoute(paths, httpMethod, method);
        } else {
            logger.error("AddRoute, Path \'" + path + "\' is Illegal!");
        }
    }

    @Override
    public void addRoute(String[] paths, org.opticaline.alaptus.core.config.HttpMethod httpMethod, org.opticaline.alaptus.core.route.RouteBean method) {
        if (paths.length == 1) {
            this.self.put(httpMethod, method);
        } else {
            AbstractRouteNode abstractRouteNode = new RouteContext();
            String[] orders = ArrayUtils.remove(paths, 0);
            abstractRouteNode.addRoute(orders, httpMethod, method);
            try {
                this.routeNodeMap.put(this.pathPretreatment(orders[0]), abstractRouteNode);
            } catch (org.opticaline.alaptus.core.exception.IllegalPathException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public org.opticaline.alaptus.core.route.RouteBean getRoute(String path, org.opticaline.alaptus.core.config.HttpMethod httpMethod) {
        String[] paths;
        if (path.equals("/")) {
            paths = new String[]{""};
        } else {
            paths = path.split("/");
        }
        if (paths.length >= 1 && paths[0].length() == 0) {
            return this.getRoute(paths, httpMethod);
        } else {
            logger.error("AddRoute, Path \'" + path + "\' is Illegal!");
            return null;
        }
    }

    @Override
    public org.opticaline.alaptus.core.route.RouteBean getRoute(String[] paths, org.opticaline.alaptus.core.config.HttpMethod httpMethod) {
        if (paths.length == 1) {
            return this.self.get(httpMethod);
        } else {
            String[] orders = ArrayUtils.remove(paths, 0);
            org.opticaline.alaptus.core.route.RouteBean routeBean = this.getRouteForString(orders, httpMethod);
            if (routeBean != null) {
                return routeBean;
            } else {
                return this.getRouteForRegex(orders, httpMethod);
            }
        }
    }

    private org.opticaline.alaptus.core.route.RouteBean getRouteForRegex(String[] paths, org.opticaline.alaptus.core.config.HttpMethod httpMethod) {
        for (String key : this.routeNodeMap.keySet()) {
            if (paths[0].matches(key)) {
                this.setUriParameter(paths[0], key);
                AbstractRouteNode abstractRouteNode = this.routeNodeMap.get(key);
                if (abstractRouteNode != null) {
                    return abstractRouteNode.getRoute(paths, httpMethod);
                }
            }
        }
        return null;
    }

    private org.opticaline.alaptus.core.route.RouteBean getRouteForString(String[] paths, org.opticaline.alaptus.core.config.HttpMethod httpMethod) {
        AbstractRouteNode abstractRouteNode = this.routeNodeMap.get(paths[0]);
        if (abstractRouteNode != null) {
            return abstractRouteNode.getRoute(paths, httpMethod);
        }
        return null;
    }

    private void setUriParameter(String uri, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            Map<String, Object> params = ContextFactory.getApplicationContext().getUriParameters();
            String[] names = regex.split("[<|>]");
            for (int i = 1; i < names.length; i += 2) {
                params.put(names[i], matcher.group(names[i]));
            }
            ContextFactory.getApplicationContext().setUriParameters(params);

        }
    }
}
