package org.opticaline.task;

import java.lang.reflect.InvocationTargetException;
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
    private List<Mission> nextMissions = new ArrayList<>();

    private List<Integer> getMinimums(long[] times) {
        int low = -1;
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < times.length; i++) {
            if (low == -1 || times[i] < times[low]) {
                low = i;
                index.clear();
                index.add(i);
            } else {
                index.add(i);
            }
        }
        return index;
    }

    @Override
    public void run() {
        while (true) {
            if (missions.size() == 0) {
                forever = true;
            } else {
                //计算下一次执行任务的时间点并将需要执行的任务加载到 nextMissions 中
                long now = System.currentTimeMillis(), round = now / 1000 * 1000;
                long[] times = new long[missions.size()];
                for (int i = 0; i < missions.size(); i++) {
                    times[i] = CronUtils.nextLong(crones.get(i), round);
                }
                List<Integer> minimums = getMinimums(times);
                for(int i : minimums){
                    sleep_millis = times[i] - now;
                    nextMissions.add(missions.get(i));
                }
            }

            if (forever) {
                this.waitForever();
            } else {
                this.waitFor(sleep_millis);
            }
            nextMissions.forEach(this::runMission);
            nextMissions.clear();
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
