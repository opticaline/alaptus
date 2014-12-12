package org.opticaline.alaptus.core.format;

import java.util.Map;

/**
 * Created by Nathan on 14-9-3.
 */
public class FloatTransaction implements Transaction {
    @Override
    public Object format(Map<String, Object> urlMap, Map<String, String[]> parameters, String name, Object... parameter) {
        return Float.valueOf((String) parameter[0]);
    }
}
