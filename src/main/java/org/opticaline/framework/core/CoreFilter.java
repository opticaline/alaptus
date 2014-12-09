package org.opticaline.framework.core;

import org.opticaline.framework.core.loader.LoaderController;

import javax.servlet.*;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created by Nathan on 2014/12/5.
 */
public class CoreFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String pkg = filterConfig.getInitParameter("package-scan");
        //初始化框架
        //1.运行PluginLoadModule加载插件
        LoaderController loader = new LoaderController(new String[]{pkg, "org.opticaline"});
        loader.load();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Writer writer = response.getWriter();
        writer.append("Hello");
        writer.flush();

    }

    @Override
    public void destroy() {

    }


}
