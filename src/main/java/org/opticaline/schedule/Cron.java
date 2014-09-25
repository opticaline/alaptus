package org.opticaline.schedule;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.opticaline.framework.core.annotation.Param;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nathan on 14-9-1.
 */
public class Cron {
    private String cron;
    private Pattern pattern = Pattern.compile("(?<start>([0-9]?[0-9])|\\*)(-(?<end>[0-9]?[0-9]))?(/(?<each>\\d+))?");

    private Cron(String cron) {
        this.cron = cron;
    }

    private boolean validate() {
        return this.cron.matches(new StringBuilder()
                .append("((([0-5]?[0-9])|\\*)(-[0-5]?[0-9])?(/\\d+)?\\s+){1,2}")
                .append("(([0-2]?[0-9])|\\*)(-[0-2]?[0-9])?(/\\d+)?\\s+")
                .append("(([0-3]?[0-9])|\\*)(-[0-3]?[0-9])?(/\\d+)?\\s+")
                .append("(([0-1]?[0-9])|\\*|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(-[0-1]?[0-9]||JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)?(/\\d+)?\\s+")
                .append("(([0-7])|\\*|SUN|MON|TUE|WED|THU|FRI|SAT)(-[0-7]|SUN|MON|TUE|WED|THU|FRI|SAT)?(/\\d+)?").toString());
    }

    private LocalDateTime nextTime() {
        String temp[] = this.cron.split("\\s+");
        LocalDateTime next = LocalDateTime.now().withNano(0);
        for (int i = 0; i < temp.length; i++) {
            next = subExpAnalysis(temp[i], temp.length - i, next);
        }
        return next;
    }

    private LocalDateTime subExpAnalysis(String exp, int level, LocalDateTime now) {
        Matcher matcher = pattern.matcher(exp);
        matcher.find();
        String temp = matcher.group("start");
        int start = temp.equals("*") ? 0 : Integer.valueOf(temp);
        temp = matcher.group("end");
        int end = StringUtils.isEmpty(temp) ? 60 : Integer.valueOf(temp);
        temp = matcher.group("each");
        int each = StringUtils.isEmpty(temp) ? 1 : Integer.valueOf(temp);
        return subExpAnalysis(start, end, each, level, now);
    }

    /**
     * 分配对应处理函数进行处理
     * <p>
     * Cron表达式可以为如下形式
     * <p>
     * ┌───────────────────────
     * │ 6位     : * * * * * *
     * ├───────────────────────
     * │ 5位     :   * * * * *
     * ├───────────────────────
     * │对应level: 6 5 4 3 2 1
     * └───────────────────────
     * <p>
     * Level表:
     * ┌───┬────────────┐
     * │ 1 │ dayOfWeek  │
     * ├───┼────────────┤
     * │ 2 │ month      │
     * ├───┼────────────┤
     * │ 3 │ dayOfMonth │
     * ├───┼────────────┤
     * │ 4 │ hour       │
     * ├───┼────────────┤
     * │ 5 │ minute     │
     * ├───┼────────────┤
     * │ 6 │ second     │
     * └───┴────────────┘
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param each  循环间隔
     * @param level 对应控制
     * @param now   当前时间
     * @return 计算过后的时间
     */
    private LocalDateTime subExpAnalysis(int start, int end, int each, int level, LocalDateTime now) {
        switch (level) {
            case 1:
                //dayOfWeek
                now = dayOfWeek(start, end, each, now);
                break;
            case 2:
                //month
                now = month(start, end, each, now);
                break;
            case 3:
                //dayOfMonth
                now = dayOfMonth(start, end, each, now);
                break;
            case 4:
                //hour
                now = hour(start, end, each, now);
                break;
            case 5:
                //minute
                now = minute(start, end, each, now);
                break;
            case 6:
                //second
                now = second(start, end, each, now);
                break;
            default:
                //error
                System.out.println("Level: " + level + " undefined");
                break;
        }
        System.out.println(now);
        return now;
    }


    /**
     * Level 1:
     * 对Cron中周的部分进行计算
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param each  间隔时间
     * @param now   当前时间
     * @return 计算过后的时间
     */
    private LocalDateTime dayOfWeek(int start, int end, int each, LocalDateTime now) {
        int dayOfWeek = now.getDayOfWeek().getValue();
        if (each != -1) {
            dayOfWeek = (dayOfWeek / each + 1) * each - dayOfWeek;
            now = now.plusDays(dayOfWeek);
        }
        dayOfWeek = now.getDayOfWeek().getValue();
        if (dayOfWeek < start || dayOfWeek > end) {
            now = now.plusDays(60 - dayOfWeek);
        }
        return now;
    }


    /**
     * Level 2:
     * 对Cron中月的部分进行计算
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param each  间隔时间
     * @param now   当前时间
     * @return 计算过后的时间
     */
    private LocalDateTime month(int start, int end, int each, LocalDateTime now) {
        int month = now.getMonthValue();
        if (each != -1) {
            month = (month / each + 1) * each - month;
            now = now.plusMonths(month);
        }
        month = now.getMonthValue();
        if (month < start || month > end) {
            now = now.plusMonths(60 - month);
        }
        return now;
    }


    /**
     * Level 3:
     * 对Cron中日的部分进行计算
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param each  间隔时间
     * @param now   当前时间
     * @return 计算过后的时间
     */
    private LocalDateTime dayOfMonth(int start, int end, int each, LocalDateTime now) {
        int dayOfMonth = now.getDayOfMonth();
        if (each != -1) {
            dayOfMonth = (dayOfMonth / each + 1) * each - dayOfMonth;
            now = now.plusDays(dayOfMonth);
        }
        dayOfMonth = now.getDayOfMonth();
        if (dayOfMonth < start || dayOfMonth > end) {
            now = now.plusDays(60 - dayOfMonth);
        }
        return now;
    }


    /**
     * Level 4:
     * 对Cron中时的部分进行计算
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param each  间隔时间
     * @param now   当前时间
     * @return 计算过后的时间
     */
    private LocalDateTime hour(int start, int end, int each, LocalDateTime now) {
        int hour = now.getHour();
        if (each != -1) {
            hour = (hour / each + 1) * each - hour;
            now = now.plusHours(hour);
        }
        hour = now.getHour();
        if (hour < start || hour > end) {
            now = now.plusHours(60 - hour);
        }
        return now;
    }


    /**
     * Level 5:
     * 对Cron中分的部分进行计算
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param each  间隔时间
     * @param now   当前时间
     * @return 计算过后的时间
     */
    private LocalDateTime minute(int start, int end, int each, LocalDateTime now) {
        int minute = now.getMinute();
        if (each != -1) {
            minute = (minute / each + 1) * each - minute;
            now = now.plusMinutes(minute);
        }
        minute = now.getMinute();
        if (minute < start || minute > end) {
            now = now.plusMinutes(60 - minute);
        }
        return now;
    }

    /**
     * Level 6:
     * 对Cron中秒的部分进行计算
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param each  间隔时间
     * @param now   当前时间
     * @return 计算过后的时间
     */
    private LocalDateTime second(int start, int end, int each, LocalDateTime now) {
        int second = now.getSecond();
        if (each != -1) {
            second = (second / each + 1) * each - second;
            now = now.plusSeconds(second);
        }
        second = now.getSecond();
        if (second < start || second > end) {
            now = now.plusSeconds(60 - second);
        }
        return now;
    }


    public static LocalDateTime load(String cron) throws IllegalCronException {
        Cron c = new Cron(cron);
        if (c.validate()) {
            return c.nextTime();
        } else {
            System.out.println("cron expression is illegal");
            throw new IllegalCronException();
        }
    }
}
