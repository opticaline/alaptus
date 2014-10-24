package org.opticaline.task;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

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
        private String cron;

        public CronCal setCron(String cron) {
            this.cron = cron;
            return this;
        }

        public static int getLevelMaxLength(LocalDateTime now, int level) {
            return new int[]{60, 60, 24, now.getMonth().maxLength() + 1, 13, 7}[level];
        }

        public LocalDateTime next(LocalDateTime now) {
            String[] exp = cron.split("\\s+");
            for (int i = 0; i < exp.length; i++) {
                now = exec(exp[i], i, now);
            }
            return now;
        }

        /*private LocalDateTime checkSection(LocalDateTime now, int[] rightTime, int level) {
            if (rightTime != null && rightTime.length > 0) {
                int num = getTimeByLevel(now, level);
                if (num < rightTime[0]) {
                    now = execTimeByLevel(now, level, rightTime[0]);
                } else if (num > rightTime[rightTime.length - 1]) {
                    int temp = getTimeByLevel(now, level + 1);
                    now = execTimeByLevel(now, level + 1, temp + 1);
                    now = execTimeByLevel(now, level, rightTime[0]);
                    for(int i = 0 ; i < level;i++)
                        now = execTimeByLevel(now, i, 0);
                    // now = checkSection(now);
                }
            }
            return now;
        }*/

        private LocalDateTime exec(String str, int level, LocalDateTime now) {
            String[] t = str.split("/");
            String section = null;
            int each = 0;
            int[] rightTime = new int[0];
            if (t.length == 2) {
                section = t[0];
                each = t[1].equals("*") ? 1 : Integer.valueOf(t[1]);
                if (level == 6) {
                    section = section.replace("7", "0");
                }
                String[] s = section.split("-");
                int begin = Integer.valueOf(s[0]), end = 0;
                if (s.length == 2) {
                    end = Integer.valueOf(s[1]);
                }
                rightTime = new int[(end - begin + 1) <= 0 ? 1 : (end - begin + 1)];
                for (int i = begin; i <= end; i++) {
                    rightTime[i - begin] = i;
                }
                rightTime[0] = begin;
            } else if (t.length == 1) {
                if (t[0].equals("*")) {
                    return now;
                }
                each = Integer.valueOf(t[0]);
            }
            int temp = getTimeByLevel(now, level);
            if (temp % each != 0) {
                temp = temp / each * each + each;
            }
            return execTimeByLevel(now, level, temp);
        }

        private LocalDateTime execTimeByLevel(LocalDateTime now, int level, int count) {
            if (level < 7) {
                int max = getLevelMaxLength(now, level);
                if (count >= max) {
                    count = count - max;
                    execTimeByLevel(now, level + 1, getTimeByLevel(now, level + 1) + 1);
                }
            }
            if ((level == 4 || level == 3) && count == 0) {
                count = 1;
            }
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
                    return now.getMonth().getValue();
                case 5:
                    return now.getDayOfWeek().getValue();
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        LocalDateTime time = LocalDateTime.of(2014, 12, 1, 23, 5, 51);
        System.out.println(CronUtils.nextTime("5 1-5/5 22/* * * *", time));
    }
}
