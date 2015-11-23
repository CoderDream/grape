package guda.grape.timeout;

/**
 * Created by well on 15/3/17.
 */
public interface Command {

    /**
     * 处理完成后需要将order_event表纪录删除
     * @param taskEvent
     * @param taskFuture
     */
    public void exec(TaskEvent taskEvent, TaskFuture taskFuture);


    public String getEventType();



}
