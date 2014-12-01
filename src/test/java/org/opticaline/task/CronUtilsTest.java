package org.opticaline.task;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;

/**
 * Created by Nathan on 2014/10/31.
 */
public class CronUtilsTest {
    @Test
    public void testNextTime() {
        String[] cron = new String[]{
                "*/1 * * * * *",
                "1 * * * * *",
                "5 6-9/5 22/* * * *"
        };
        LocalDateTime[] params = new LocalDateTime[]{
                LocalDateTime.of(2014, 12, 31, 23, 59, 59),
                LocalDateTime.of(2014, 12, 1, 23, 6, 51),
                LocalDateTime.of(2014, 12, 31, 23, 59, 59)
        };
        LocalDateTime[] results = new LocalDateTime[]{
                LocalDateTime.of(2015, 1, 1, 0, 0, 0),
                LocalDateTime.of(2014, 12, 1, 23, 6, 52),
                LocalDateTime.of(2015, 1, 1, 22, 6, 0)
        };

        for (int i = 0; i < cron.length; i++) {
            assertTrue(CronUtils.nextTime(cron[i], params[i]).equals(results[i]));
        }
    }
}
