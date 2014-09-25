package org.opticaline.framework.core.format;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

/**
 * Created by Nathan on 2014/9/12.
 */
public class StringArrayTransaction implements Transaction {

    @Override
    public Object format(Map<String, Object> urlMap, Map<String, String[]> parameters, String name, Object[] parameter) {
        String[] array = (String[]) parameter[0];
        Object temp = urlMap.get(name);
        if (temp != null) {
            array = ArrayUtils.add(array, (String) temp);
        }
        return array;
    }

}