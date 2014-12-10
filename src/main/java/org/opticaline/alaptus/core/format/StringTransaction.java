package org.opticaline.alaptus.core.format;

import org.opticaline.alaptus.core.format.Transaction;

import java.util.Map;

/**
 * Created by Nathan on 14-9-3.
 */
public class StringTransaction implements Transaction {

    @Override
    public Object format(Map<String, Object> urlMap, Map<String, String[]> parameters, String name, Object[] parameter) {
        return parameter[0].toString();
    }

}
