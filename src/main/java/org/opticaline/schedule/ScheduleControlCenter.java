package org.opticaline.schedule;

import org.opticaline.framework.core.ControlCenter;
import org.opticaline.schedule.annotation.Task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by Nathan on 14-8-29.
 */
public class ScheduleControlCenter implements ControlCenter {

    private void loadTask() {

    }

    @Override
    public void control() {
        new MainTaskThread(new TreeMap<LocalDateTime, Method>() {{
            try {
                Class cls = ScheduleControlCenter.class;
                Method staticMethod = cls.getDeclaredMethod("test");
                Task task = (Task)staticMethod.getAnnotations()[0];
                put(Cron.load(task.value()), staticMethod);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalCronException e) {
                e.printStackTrace();
            }
        }}).start();
    }

    class MainTaskThread extends Thread {
        private Map<LocalDateTime, Method> timerList = new TreeMap<LocalDateTime, Method>();

        MainTaskThread(TreeMap timerList) {
            this.timerList = timerList;
        }

        @Override
        public void run() {
            for (; ; ) {
                Set keys = timerList.keySet();
                Iterator iterator = keys.iterator();
                if (iterator.hasNext()) {
                    LocalDateTime timer = (LocalDateTime) iterator.next();
                    Method method = timerList.get(timer);
                    long hasMills = ChronoUnit.MILLIS.between(LocalDateTime.now(), timer);
                    try {
                        if (hasMills > 0) {
                            Thread.sleep(hasMills);
                            new InvokeMethodAfter(method).start();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        timerList.remove(timer);
                        timer = timer.plusSeconds(5);
                        timerList.put(timer, method);
                    }
                } else {
                    break;
                }
            }
            System.out.println("Task done!");
        }
    }

    class InvokeMethodAfter extends Thread {
        private Method method;

        InvokeMethodAfter(Method method) {
            this.method = method;
        }

        @Override
        public void run() {
            try {
                this.method.invoke(null);
            } catch (InvocationTargetException | IllegalAccessException e) {
                System.out.println("Error");
            }
        }
    }

    public static void main(String[] args) throws IllegalCronException {
        new ScheduleControlCenter().control();
    }

    @Task("*/5 * * * * *")
    public static void test() {
        System.out.println("test");
    }
}
