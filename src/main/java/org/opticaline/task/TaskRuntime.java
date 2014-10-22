package org.opticaline.task;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 2014/10/16.
 */
public class TaskRuntime implements Runnable {
    private long sleep_millis = 0;
    private boolean forever = false;
    private List<Mission> missions = new ArrayList<>();
    private List<String> crones = new ArrayList<>();

    private List<Mission> nextMissions = new ArrayList<>();

    @Override
    public void run() {
        while (true) {
            System.out.println(LocalDateTime.now());
            for (int i = 0; i < nextMissions.size(); i++) {
                runMission(nextMissions.get(i));
            }
            //计算下一次执行任务的时间点并将需要执行的任务加载到 nextMissions 中
            if (true) {
                this.waitForever();
                forever = true;
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
