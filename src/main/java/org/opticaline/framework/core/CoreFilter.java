package org.opticaline.framework.core;

import org.opticaline.framework.core.loader.LoaderController;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by Nathan on 2014/12/5.
 */
public class CoreFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //初始化框架
        //1.运行PluginLoadModule加载插件
        LoaderController loader = new LoaderController();
        loader.load();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
