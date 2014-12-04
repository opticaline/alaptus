package org.opticaline.framework.core;

import org.opticaline.framework.core.format.Transaction;
import org.opticaline.framework.core.route.RouteBean;
import org.opticaline.framework.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by Nathan on 2014/9/12.
 */
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public static Object handler(RouteBean routeBean, Map<String, String[]> parameterMap) throws RuntimeException {
        Object result;
        try {
            result = routeBean.getMethod().invoke(routeBean.getClazz(), argumentsHandler(routeBean, parameterMap));
            logger.debug("Execute - {}", routeBean.getMethod());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException();
        }
        return result;
    }

    private static Object[] argumentsHandler(RouteBean routeBean, Map<String, String[]> parameterMap) {
        String[] paramNames = routeBean.getParamNames();
        Object[] arguments = new Object[paramNames.length];
        //获取url路径中截取的参数
        Map<String, Object> urlParams = ContextFactory.getApplicationContext().getUriParameters();
        for (int i = 0; i < paramNames.length; i++) {
            Class clazz = routeBean.getParamType(i);
            //分别从url和request中获取参数并封装
            if (isJDKType(clazz)) {
                arguments[i] = getParam(routeBean.getParamNames(i), clazz, urlParams, parameterMap);
                //url路径参数的注入优先级高
                //TODO 下一步解决url参数的数组形式问题
                if (arguments[i] != null) {
                    Transaction transaction = ContextFactory.getApplicationContext()
                            .getTransaction(clazz);
                    if (transaction != null) {
                        arguments[i] = transaction.format(urlParams,
                                parameterMap, routeBean.getParamNames(i), arguments[i]);
                    } else {
                        logger.debug("Not found Transaction for type {}.", clazz);
                    }
                } else if (routeBean.getParamNotNull(i)) {
                    if (!clazz.isPrimitive()) {
                        try {
                            arguments[i] = clazz.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            logger.error("Can't init parameter type {}.", clazz);
                        }
                    }
                }
            } else {
                Object bean = null;
                try {
                    //获取Bean的类型之后制造Bean实例
                    bean = clazz.newInstance();
                    //封装成javabean
                    BeanUtils.request2Bean(parameterMap, bean);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                arguments[i] = bean;
                //java bean
            }

        }
        return arguments;
    }

    private static Object getParam(String name, Class type, Map<String, Object> url, Map<String, String[]> req) {
        Object param = req.get(name);
        Object uriParam = url.get(name);
        Object result = null;
        if (uriParam != null) {
            result = uriParam;
        } else {
            if (type.isArray()) {
                result = type.cast(param);
            } else {
                String[] params = (String[]) param;
                if (params.length > 0) {
                    result = params[0];
                }
            }
        }
        return result;
    }

    private static boolean isJDKType(Class clazz) {
        String className = clazz.toString();
        return !className.startsWith("class") || className.startsWith("class java.lang.") || className.startsWith("class [Ljava.lang.");
    }
}
