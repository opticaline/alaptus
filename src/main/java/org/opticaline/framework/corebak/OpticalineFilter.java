package org.opticaline.framework.corebak;

import org.dom4j.DocumentException;
import org.opticaline.framework.corebak.config.FrameworkSettings;
import org.opticaline.framework.corebak.exception.SetupException;
import org.opticaline.framework.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Nathan on 14-9-4.
 */
public class OpticalineFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(OpticalineFilter.class);
    private Framework framework;

    @Override
    public void init(FilterConfig config) throws ServletException {
        String applicationSettingsPath = config.getInitParameter("ApplicationContext");
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
            framework = new OpticalineFramework(settings);
            framework.scan();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (SetupException e) {
            logger.error(e.getMessage());
        }
    }
}
