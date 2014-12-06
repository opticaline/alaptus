package org.opticaline.framework.corebak.format;

import java.util.Map;

/**
 * Created by Nathan on 14-8-27.
 */
public class IntTransaction implements Transaction {

    @Override
    public Object format(Map<String, Object> urlMap, Map<String, String[]> parameters, String name, Object[] parameter) {
        try {
            return Integer.valueOf((String) parameter[0]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
