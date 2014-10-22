package org.opticaline.task;

import org.opticaline.task.annotation.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Created by Nathan on 2014/10/16.
 */
public class TaskControlCenter {
    private static final Logger logger = LoggerFactory.getLogger(TaskControlCenter.class);
    private static TaskControlCenter center;
    private TaskRuntime runtime;

    private TaskControlCenter() {
        runtime = new TaskRuntime();
        new Thread(runtime).start();
    }

    public static synchronized TaskControlCenter setup() {
        if (center == null) {
            logger.debug("TaskControlCenter initialize...");
            center = new TaskControlCenter();
        }
        return center;
    }

    public void addMission(String cron, Object object, Method method) throws IllegalCronExpException {
        Mission mission = new Mission();
        mission.setMethod(method);
        mission.setObject(object);
        runtime.addMission(cron, mission);
    }

    public void addMission(String cron, Class<Object> clazz) throws IllegalCronExpException {
        try {
            Method execute = clazz.getDeclaredMethod("execute");
            center.addMission(cron, clazz.newInstance(), execute);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalCronExpException {
        TaskControlCenter tcc = TaskControlCenter.setup();
        Method test = TaskControlCenter.class.getDeclaredMethod("test");
        tcc.addMission("* * * * * *", new Object(), test);
    }

    @Task("* * * * * *")
    public static void test() {
        System.out.println("test()" + LocalDateTime.now());
    }
}
