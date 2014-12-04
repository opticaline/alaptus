package org.opticaline.framework.core;

import org.opticaline.framework.core.bootstrap.AnnotationScan;
import org.opticaline.framework.core.config.FrameworkSettings;
import org.opticaline.framework.core.config.HttpMethod;
import org.opticaline.framework.core.exception.SetupException;
import org.opticaline.framework.core.route.RouteBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Created by Nathan on 14-8-25.
 */
public class OpticalineFramework implements Framework {
    private static final Logger logger = LoggerFactory.getLogger(OpticalineFramework.class);
    private FrameworkSettings frameworkSettings;

    public OpticalineFramework(FrameworkSettings settings) {
        frameworkSettings = settings;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) {
        HttpMethod method = HttpMethod.valueOf(req.getMethod());
        String uri = req.getRequestURI();
        logger.debug("{} {} {}", req.getProtocol(), uri, method);
        ContextFactory.getApplicationContext().setRequest(req);
        RouteBean action = ContextFactory.getRouteContext().getRoute(uri, method);
        Object result = null;
        if (action != null) {
            try {
                result = this.run(action, req.getParameterMap());
            } catch (Exception e) {
                resp.setStatus(500);
                throw e;
            }
        } else {
            resp.setStatus(404);
            //404 || static files
        }
        try {
            if (result != null) {
                Writer write = null;
                write = resp.getWriter();
                write.append(result.toString());
                write.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object run(RouteBean routeBean, Map<String, String[]> parameterMap) {
        return Controller.handler(routeBean, parameterMap);
    }

    @Override
    public void scan() throws SetupException {
        AnnotationScan.scan();
    }
}
