package org.opticaline.task;

import java.time.LocalDateTime;

/**
 * Created by Nathan on 2014/10/22.
 */
public class CronUtils {
    private static String CRON_REGEX;
    private static CronCal CAL = new CronCal();

    static {
        initCronRegex();
    }

    private static void initCronRegex() {
        String mouthRegex = join(Mouths.values());
        String dayRegex = join(DaysOfWeek.values());

        CRON_REGEX = "((([0-5]?[0-9])|\\*)(-[0-5]?[0-9])?(/\\d+)?\\s+){1,2}" +
                "(([0-5]?[0-9])|\\*)(-[0-5]?[0-9])?(/\\d+)?\\s+){1,2}" +
                "(([0-2]?[0-9])|\\*)(-[0-2]?[0-9])?(/\\d+)?\\s+" +
                "(([0-3]?[0-9])|\\*)(-[0-3]?[0-9])?(/\\d+)?\\s+" +
                "(([0-1]?[0-9])|\\*|" + mouthRegex + ")(-[0-1]?[0-9]|" + mouthRegex + ")?(/\\d+)?\\s+" +
                "(([0-7])|\\*|" + dayRegex + ")(-[0-7]|" + dayRegex + ")?(/\\d+)?";
    }

    private static String join(Enum[] enums) {
        StringBuffer sb = new StringBuffer();
        for (Enum enm : enums) {
            sb.append(enm).append("|");
        }
        return sb.substring(0, sb.length() - 1);
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
        if(validate(cron)){
            //TODO 当检测到cron表达式不合法时的异常处理
            //throw new IllegalCronExpException();
        }
        //TODO 将cron表达式中的字符串替换为对应的数字，如JAN应替换为Mouths.JAN.value()
        return cron;
    }

    public enum Mouths {
        JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8), SEP(9), OCT(10), NOV(11), DEC(12);
        private int value;

        Mouths(int i) {
            this.value = i;
        }

        public int value() {
            return this.value;
        }

        public Mouths getOf(String str) {
            for (Mouths mouth : Mouths.values()) {
                if (mouth.toString().equalsIgnoreCase(str)) {
                    return mouth;
                }
            }
            return null;
        }

        public Mouths getOf(int i) {
            for (Mouths mouth : Mouths.values()) {
                if (mouth.value == i) {
                    return mouth;
                }
            }
            return null;
        }
    }

    public enum DaysOfWeek {
        SUN(0), MON(1), TUE(2), WED(3), THU(4), FRI(5), SAT(6);
        private int value;

        DaysOfWeek(int i) {
            this.value = i;
        }

        public int value() {
            return this.value;
        }

        public DaysOfWeek getOf(String str) {
            for (DaysOfWeek daysOfWeek : DaysOfWeek.values()) {
                if (daysOfWeek.toString().equalsIgnoreCase(str)) {
                    return daysOfWeek;
                }
            }
            return null;
        }

        public DaysOfWeek getOf(int i) {
            for (DaysOfWeek daysOfWeek : DaysOfWeek.values()) {
                if (daysOfWeek.value == i) {
                    return daysOfWeek;
                }
            }
            return null;
        }
    }

    private static class CronCal {
        private String cron;
        private int[] level_min;
        private int[] level_max;
        private boolean useMouth = true;

        public CronCal setCron(String cron) {
            this.cron = cron;
            return this;
        }

        public int getLevelMaxLength(int level) {
            return level_max[level];
        }

        public int getLevelMinLength(int level) {
            return level_min[level];
        }

        public void setLevel_max(int level, int max) {
            if (level_max[level] > max) {
                level_max[level] = max;
            }
        }

        public void setLevel_min(int level, int min) {
            if (level_min[level] < min) {
                level_min[level] = min;
            }
        }

        public LocalDateTime next(LocalDateTime now) {
            level_max = new int[]{60, 60, 24, now.getMonth().maxLength() + 1, 13, 7};
            level_min = new int[]{0, 0, 0, 1, 1, 0};
            String[] exp = cron.split("\\s+");
            for (int i = 0; i < exp.length; i++) {
                now = exec(exp[i], i, now);
            }
            return now;
        }

        private LocalDateTime exec(String str, int level, LocalDateTime now) {
            String[] t = str.split("/");
            String section = null;
            int each = 0;
            if (t.length == 2) {
                section = t[0];
                each = t[1].equals("*") ? 1 : Integer.valueOf(t[1]);
                if (level == 6) {
                    section = section.replace("7", "0");
                }
                if (!section.equals("*")) {
                    String[] s = section.split("-");
                    int max = Integer.valueOf(s[0]), min = max;
                    if (s.length == 2) {
                        max = Integer.valueOf(s[1]);
                    }
                    setLevel_max(level, max);
                    setLevel_min(level, min);
                }
            } else if (t.length == 1) {
                if (t[0].equals("*")) {
                    return now;
                }
                each = Integer.valueOf(t[0]);
            }
            int temp = getTimeByLevel(now, level);
            temp = temp / each * each + each;
            return execTimeByLevel(now, level, temp);
        }

        private LocalDateTime execTimeByLevel(LocalDateTime now, int level, int count) {
            if (level < 7) {
                int max = getLevelMaxLength(level), min = getLevelMinLength(level);
                while (count >= max) {
                    count = count - max;
                    int next, iNext = level;
                    if (iNext == 4) {
                        iNext = 10;
                        next = now.getYear() + 1;
                    } else {
                        iNext += 1;
                        next = getTimeByLevel(now, iNext) + 1;
                    }
                    now = execTimeByLevel(now, iNext, next);
                    for (int i = 0; i < level; i++) {
                        now = execTimeByLevel(now, i, getLevelMinLength(i));
                    }
                }
                if (count < min) {
                    count = min;
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
                case 10:
                    return now.withYear(count);
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
        LocalDateTime time = LocalDateTime.of(2014, 12, 31, 23, 59, 59);
        System.out.println(CronUtils.nextTime("*/1 * * * * ?", time));
    }
}
