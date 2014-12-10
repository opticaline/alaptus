package org.opticaline.alaptus.core.bootstrap;

import org.opticaline.alaptus.core.ApplicationContext;
import org.opticaline.alaptus.core.annotation.Param;
import org.opticaline.alaptus.core.annotation.ParamTransaction;
import org.opticaline.alaptus.core.annotation.Route;
import org.opticaline.alaptus.core.format.Transaction;
import org.opticaline.alaptus.core.route.RouteBean;
import org.opticaline.alaptus.core.route.RouteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Nathan on 2014/9/12.
 */
public abstract class AnonymousLoader {

    void load(Class clazz, Annotation annotation) throws IllegalAccessException, InstantiationException {
    }

    void load(Class clazz, Method method, Annotation annotation) throws IllegalAccessException, InstantiationException {
    }


    public static class RouteLoader extends org.opticaline.alaptus.core.bootstrap.AnonymousLoader {
        private static final Logger logger = LoggerFactory.getLogger(RouteLoader.class);
        private RouteContext routeContext;

        public RouteLoader(RouteContext routeContext) {
            this.routeContext = routeContext;
        }

        public void load(Class clazz, Method method, Annotation annotation) throws IllegalAccessException, InstantiationException {
            Route route = (Route) annotation;
            RouteBean routeBean = new RouteBean(clazz.newInstance(), method);
            Object[] temps = this.getParameterDetails(method);
            routeBean.setParamNames((String[]) temps[0]);
            routeBean.setParamTypes(method.getParameterTypes());
            routeBean.setParamNotNull((boolean[]) temps[1]);
            this.routeContext.addRoute(route.value(), route.method(), routeBean);
            logger.debug("Add route: {} - {} {}", method.getName(), route.value(), route.method());
        }

        private Object[] getParameterDetails(Method method) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            String[] paramNames = new String[parameterAnnotations.length];
            boolean[] notnull = new boolean[parameterAnnotations.length];
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (int j = 0; j < parameterAnnotations[i].length; j++) {
                    if (parameterAnnotations[i][j] instanceof Param) {
                        Param param = (Param) parameterAnnotations[i][j];
                        paramNames[i] = param.value();
                        notnull[i] = param.notNull();
                    }
                }
            }
            return new Object[]{paramNames, notnull};
        }
    }

    public static class ParamTransactionLoader extends org.opticaline.alaptus.core.bootstrap.AnonymousLoader {
        private static final Logger logger = LoggerFactory.getLogger(ParamTransactionLoader.class);
        private ApplicationContext context;

        public ParamTransactionLoader(ApplicationContext context) {
            this.context = context;
        }

        public void load(Class clazz, Annotation annotation) throws IllegalAccessException, InstantiationException {
            ParamTransaction paramTransactions = (ParamTransaction) annotation;
            this.context.appendTransactions(paramTransactions.value(), (Transaction) clazz.newInstance());
            logger.debug("Add transaction: {}, String -> {}", clazz.getClass(), paramTransactions.value());
        }
    }
}
