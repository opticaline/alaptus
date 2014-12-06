package org.opticaline.framework.corebak;

import org.opticaline.framework.corebak.exception.SetupException;
import org.opticaline.framework.corebak.route.RouteBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Nathan on 14-8-25.
 */
public interface Framework {
    void service(HttpServletRequest req, HttpServletResponse resp);

    void scan() throws SetupException;

    Object run(RouteBean routeBean, Map<String, String[]> parameterMap);
}
