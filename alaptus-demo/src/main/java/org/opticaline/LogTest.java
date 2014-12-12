package org.opticaline;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Nathan on 2014/12/11.
 */
public class LogTest {
    private static Log log = LogFactory.getLog(LogTest.class);

    public static void main(String[] args) {
        int[] k = {1, 2, 4, 8, 16};
        int flag = k[0] | k[3] | k[4];
        //      0b00001101
        //      0b00000000
        //      0b00000100
        //      0b00000100
        for (int i = 0; i < k.length; i++) {
            System.out.println(i + " " + ((flag & k[i]) == k[i]));
        }

    }
}
