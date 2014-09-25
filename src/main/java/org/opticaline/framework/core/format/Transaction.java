package org.opticaline.framework.core.format;

import java.util.Map;

/**
 * Created by Nathan on 14-8-27.
 */
public interface Transaction {
    public Object format(Map<String, Object> urlMap, Map<String, String[]> parameters, String name, Object... parameter);
}
