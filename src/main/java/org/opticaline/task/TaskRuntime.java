package org.opticaline.task;

import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 2014/10/16.
 */
public class TaskRuntime implements Runnable {
    private long sleep_millis = 10;
    private boolean forever = false;
    private List<Mission> missions = new ArrayList<>();
    private List<String> crones = new ArrayList<>();

    private Mission nextMissions = null;


    private int getMinimum(LocalDateTime[] times) {
        int low;
        if (times.length > 1) {
            low = 0;
            for (int i = 1; i < times.length; i++) {
                if (times[i].isBefore(times[low])) {
                    low = i;
                }
            }
            return low;
        } else if (times.length == 1) {
            return 0;
        } else {
            return -1;
        }
    }

    private long getTime(LocalDateTime time) throws ParseException {
        String[] patterns = new String[]{"yyyy-MM-dd'T'hh:mm:ss.SSS", "yyyy-MM-dd'T'hh:mm:ss"};
        return DateUtils.parseDate(time.format(DateTimeFormatter.ISO_DATE_TIME), patterns).getTime();
    }

    @Override
    public void run() {
        while (true) {
            if (nextMissions != null) {
                runMission(nextMissions);
            }
            /*for (int i = 0; i < nextMissions.size(); i++) {
                runMission(nextMissions.get(i));
                nextMissions.remove(i);
                i--;
            }*/
            //计算下一次执行任务的时间点并将需要执行的任务加载到 nextMissions 中
            LocalDateTime now = LocalDateTime.now().withNano(0);
            LocalDateTime[] times = new LocalDateTime[missions.size()];
            for (int i = 0; i < missions.size(); i++) {
                times[i] = CronUtils.nextTime(crones.get(i), now);
            }
            int minimum = getMinimum(times);
            if (minimum == -1) {
                forever = true;
            } else {
                try {
                    long time = getTime(times[minimum]);
                    sleep_millis = time - getTime(now);
                    nextMissions = missions.get(minimum);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (forever) {
                this.waitForever();
            } else {
                this.waitFor(sleep_millis);
            }
        }
    }

    public synchronized int addMission(String cron, Mission mission) throws IllegalCronExpException {
        //验证 cron 是否合法 -1
        if (!CronUtils.validate(cron)) {
            throw new IllegalCronExpException();
        }
        if (forever) {
            this.notify();
            forever = false;
        }
        missions.add(mission);
        crones.add(cron);
        return crones.size() - 1;//mission id
    }

    public synchronized void deleteMission(int id) {
        crones.set(id, null);
    }

    private void runMission(Mission mission) {
        new Thread(() -> {
            try {
                mission.getMethod().invoke(mission.getObject());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private synchronized void waitFor(long millis) {
        try {
            this.wait(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void waitForever() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
