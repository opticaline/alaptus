package org.opticaline.alaptus.core.format;

import java.util.Map;

/**
 * Created by Nathan on 14-9-3.
 */
public class BooleanTransaction implements Transaction {
    @Override
    public Object format(Map<String, Object> urlMap, Map<String, String[]> parameters, String name, Object... parameter) {
        return Boolean.valueOf((String) parameter[0]);
    }
}
