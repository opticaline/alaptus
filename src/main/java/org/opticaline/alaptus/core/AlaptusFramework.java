package org.opticaline.alaptus.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opticaline.alaptus.core.config.FrameworkSettings;
import org.opticaline.alaptus.core.config.HttpMethod;
import org.opticaline.alaptus.core.config.SettingString;
import org.opticaline.alaptus.core.exception.SetupException;
import org.opticaline.alaptus.core.loader.LoaderController;
import org.opticaline.alaptus.core.route.RouteBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Created by Nathan on 14-8-25.
 */
public class AlaptusFramework implements Framework {
    private static final Log logger = LogFactory.getLog(AlaptusFramework.class);
    private FrameworkSettings frameworkSettings;

    public AlaptusFramework(FrameworkSettings settings) {
        frameworkSettings = settings;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) {
        HttpMethod method = HttpMethod.valueOf(req.getMethod());
        String uri = req.getRequestURI();
        logger.debug(req.getProtocol() + " " + uri + " " + method);
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
        //1.运行PluginLoadModule加载插件
        LoaderController loader = new LoaderController(new String[]{
                frameworkSettings.get(SettingString.PACKAGE_SCAN), SettingString.DEFAULT_PKG});
        loader.load();
    }
}
