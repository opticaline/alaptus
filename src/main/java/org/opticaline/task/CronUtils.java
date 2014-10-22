package org.opticaline.task;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.Arrays;

/**
 * Created by Nathan on 2014/10/22.
 */
public class CronUtils {
    private static String CRON_REGEX;
    private static CronCal CAL = new CronCal();

    static {
        String mouthRegex;
        String dayRegex;
        StringBuffer sb = new StringBuffer();
        for (Mouths mouth : Mouths.values()) {
            sb.append(mouth).append("|");
        }
        mouthRegex = sb.substring(0, sb.length() - 1);

        sb = new StringBuffer();
        for (DaysOfWeek day : DaysOfWeek.values()) {
            sb.append(day).append("|");
        }
        dayRegex = sb.substring(0, sb.length() - 1);

        CRON_REGEX = "((([0-5]?[0-9])|\\*)(-[0-5]?[0-9])?(/\\d+)?\\s+){1,2}" +
                "(([0-2]?[0-9])|\\*)(-[0-2]?[0-9])?(/\\d+)?\\s+" +
                "(([0-3]?[0-9])|\\*)(-[0-3]?[0-9])?(/\\d+)?\\s+" +
                "(([0-1]?[0-9])|\\*|" + mouthRegex + ")(-[0-1]?[0-9]|" + mouthRegex + ")?(/\\d+)?\\s+" +
                "(([0-7])|\\*|" + dayRegex + ")(-[0-7]|" + dayRegex + ")?(/\\d+)?";

    }

    public static boolean validate(String cron) {
        return cron.matches(CRON_REGEX);
    }

    public static LocalDateTime nextTime(String cron, LocalDateTime now) {
        String exp = format(cron);
        return CAL.setCron(exp).next(now);
    }

    public static LocalDateTime nextTime(String cron) {
        return nextTime(cron, LocalDateTime.now());
    }

    public static String format(String cron) {
        return cron;
    }

    public enum Mouths {
        JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
    }

    public enum DaysOfWeek {
        SUN, MON, TUE, WED, THU, FRI, SAT
    }

    private static class CronCal {
        private static int[] SECTION = new int[]{59, 59, 23, 31, 12, 6};
        private String cron;

        public CronCal setCron(String cron) {
            this.cron = cron;
            return this;
        }

        public LocalDateTime next(LocalDateTime now) {
            String[] exp = cron.split("\\s+");
            for (int i = 0; i < exp.length; i++) {
                now = exec(exp[i], i, now);
            }
            return now;
        }

        private LocalDateTime exec(String str, int level, LocalDateTime now) {
            String[] t = str.split("/");
            String section = t[0];
            int each = Integer.valueOf(t.length == 2 ? t[1] : "");
            if (section.equals("*")) {
                //
            }
            if (level == 6) {
                section = section.replace("7", "0");
            }
            int max = SECTION[level];
            int temp = getTimeByLevel(now, level);
            temp = temp / each * each + each;
            temp = temp > max ? temp - max : temp;
            return execTimeByLevel(now, level, temp);
        }

        private LocalDateTime execTimeByLevel(LocalDateTime now, int level, int count) {
            switch (level) {
                case 0:
                     return now.withSecond(count);
                case 1:
                    return now.withMinute(count);
                case 2:
                    return now.withHour(count);
                case 3:
                    return now.withDayOfMonth(count);
                case 4:
                    return now.withMonth(count);
                case 5:
                    //TODO 设置下一个周几的日期
                    return now.withDayOfMonth(count);
            }
            return now;
        }

        private int getTimeByLevel(LocalDateTime now, int level) {
            switch (level) {
                case 0:
                    return now.getSecond();
                case 1:
                    return now.getMinute();
                case 2:
                    return now.getHour();
                case 3:
                    return now.getDayOfMonth();
                case 4:
                    return now.getMonth().maxLength();
                case 5:
                    return now.getDayOfWeek().getValue();
            }
            return -1;
        }
    }
}
