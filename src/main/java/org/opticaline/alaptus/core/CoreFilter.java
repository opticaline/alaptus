package org.opticaline.alaptus.core;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opticaline.alaptus.core.config.FrameworkSettings;
import org.opticaline.alaptus.core.exception.SetupException;
import org.opticaline.alaptus.utils.PathUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Nathan on 2014/12/5.
 */
public class CoreFilter implements Filter {
    private static final Log logger = LogFactory.getLog(CoreFilter.class);
    private Framework framework;

    @Override
    public void init(FilterConfig config) throws ServletException {
        //初始化框架
        String applicationSettingsPath = config.getInitParameter("ApplicationContext");
        String pkg = config.getInitParameter("package-scan");
        this.setup(applicationSettingsPath);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String uri = request.getRequestURI();
        if (uri.startsWith("/resources/") || uri.startsWith("/favicon.ico")) {
            chain.doFilter(req, resp);
        } else {
            framework.service(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    public void setup(String settingsPath) {
        try {
            FrameworkSettings settings = new FrameworkSettings(PathUtils.settingPath2Real(settingsPath));
            framework = new AlaptusFramework(settings);
            framework.scan();
        } catch (ConfigurationException | SetupException e) {
            logger.error(e.getMessage());
        }
    }

}