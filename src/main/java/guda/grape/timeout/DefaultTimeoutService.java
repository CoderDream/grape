package guda.grape.timeout;


import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

/**
 * Created by well on 15/3/17.
 */
public class DefaultTimeoutService implements InitializingBean {

    private Logger log = LoggerFactory.getLogger(DefaultTimeoutService.class);




    private TreeSet<TaskEvent> task = new TreeSet<TaskEvent>();
    private volatile boolean taskCheckRuningFlag = false;
    private Map<String, Command> commandMap;

    private TaskCheck taskCheck;

    public void register(TaskEvent taskEvent) {
        if (taskEvent == null) {
            return;
        }
        if(log.isInfoEnabled()) {
            log.info("注册超时任务:" + ReflectionToStringBuilder.toString(taskEvent));
        }
        if (!task.isEmpty()) {
            TaskEvent first = task.first();
            if (taskEvent.getExecuteTime().before(first.getExecuteTime())) {
                if (taskCheck != null) {
                    taskCheck.cancel();
                }
            }
        }
        task.add(taskEvent);
        startTaskCheck();
    }

    public synchronized void remove(TaskEvent taskEvent) {
        if (taskEvent == null) {
            return;
        }
        if(log.isInfoEnabled()) {
            log.info("取消超时任务,taskEvent=" + ReflectionToStringBuilder.toString(taskEvent));
        }
        if(!task.contains(taskEvent)){
            log.warn("取消超时任务，忽略，任务已经处理,taskEvent=" + ReflectionToStringBuilder.toString(taskEvent));
            //TODO 删除持久化任务
            return;
        }
        //如果任务是当前在进行中
        if(taskCheck!= null && taskCheck.getCurrentTaskEvent().equals(taskEvent)){
            taskCheck.cancel();
           //TODO 删除持久化任务
            startTaskCheck();
            log.warn("取消超时任务成功，当前任务在进行中，taskEvent=" + ReflectionToStringBuilder.toString(taskEvent));
        }else{
            task.remove(taskEvent);
            //TODO 删除持久化任务
            log.warn("取消超时任务成功，taskEvent=" + ReflectionToStringBuilder.toString(taskEvent));
        }

    }

    public synchronized void stop(TaskEvent taskEvent) {
        if (taskEvent == null) {
            return;
        }
        if(log.isInfoEnabled()) {
            log.info("停止超时任务,taskEvent=" + ReflectionToStringBuilder.toString(taskEvent));
        }

        //如果任务是当前在进行中
        if(taskCheck!= null && taskCheck.getCurrentTaskEvent().equals(taskEvent)){
            taskCheck.cancel();
            startTaskCheck();
            log.warn("停止超时任务成功，当前任务在进行中，taskEvent=" + ReflectionToStringBuilder.toString(taskEvent));
        }else{
            task.remove(taskEvent);
            log.warn("停止超时任务成功，taskEvent=" + ReflectionToStringBuilder.toString(taskEvent));
        }

    }


    public synchronized void startTaskCheck() {
        if (!taskCheckRuningFlag) {
            taskCheckRuningFlag = true;
            taskCheck = new TaskCheck();
            taskCheck.setDaemon(true);
            taskCheck.start();
        }


    }


    public class TaskCheck extends Thread {

        private Timer timer = new Timer();
        private TaskEvent currentTaskEvent;

        public TaskEvent getCurrentTaskEvent() {
            return currentTaskEvent;
        }

        public void cancel() {
            try {
                if (timer != null) {
                    timer.cancel();
                    taskCheckRuningFlag = false;
                    log.info("task cancel,task=" + ReflectionToStringBuilder.toString(currentTaskEvent));
                }
                this.interrupt();
            }catch(Exception e){

            }
        }

        @Override
        public void run() {
            if (task.isEmpty()) {
                taskCheckRuningFlag = false;
                return;
            }

            final TaskEvent taskEvent = task.first();
            currentTaskEvent = taskEvent;
            long time = taskEvent.getExecuteTime().getTime();
            if (time - System.currentTimeMillis() <= 0) {

                Command command = commandMap.get(taskEvent.getEventType());
                try {
                    command.exec(taskEvent, new TaskFuture() {

                        public void finish() {
                            taskCheckRuningFlag = false;
                            task.pollFirst();
                            startTaskCheck();

                        }
                    });
                }catch(Exception e){
                    log.error("",e);
                }
            } else {
                try {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Command command = commandMap.get(taskEvent.getEventType());
                            command.exec(taskEvent, new TaskFuture() {

                                public void finish() {
                                    taskCheckRuningFlag = false;
                                    task.pollFirst();
                                    startTaskCheck();
                                }
                            });
                        }
                    }, time - System.currentTimeMillis());

                } catch (Exception e) {
                    log.error("",e);
                }
            }

        }
    }


    public void afterPropertiesSet() throws Exception {

        //TODO 加载持久化任务
            startTaskCheck();


    }


    public void setCommandMap(Map<String, Command> commandMap) {
        this.commandMap = commandMap;
    }


}
