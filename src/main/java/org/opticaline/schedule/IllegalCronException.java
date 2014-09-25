package org.opticaline.schedule;

/**
 * Created by Nathan on 14-9-1.
 */
public class IllegalCronException extends Exception {

    public IllegalCronException() {
        super("cron expression is illegal");
    }
}
