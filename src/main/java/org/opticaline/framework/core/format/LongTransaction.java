package org.opticaline.framework.core.format;

import java.util.Map;

/**
 * Created by Nathan on 14-8-27.
 */
public class LongTransaction implements Transaction {

    @Override
    public Object format(Map<String, Object> urlMap, Map<String, String[]> parameters, String name, Object[] parameter) {
        return Long.valueOf((String) parameter[0]);
    }

}
