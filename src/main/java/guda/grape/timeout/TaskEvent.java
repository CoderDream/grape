package guda.grape.timeout;

import java.util.Date;

/**
 * Created by well on 15/3/18.
 */
public class TaskEvent implements Comparable<TaskEvent> {

    private Long eventId;


    private Long orderId;


    private String eventType;


    private Date executeTime;


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public int compareTo(TaskEvent o) {
        if (o == null) {
            return 0;
        }
        if (this.getExecuteTime().after(o.getExecuteTime())) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskEvent)) return false;

        TaskEvent taskEvent = (TaskEvent) o;

        if (!eventId.equals(taskEvent.eventId)) return false;
        if (!eventType.equals(taskEvent.eventType)) return false;
        if (!executeTime.equals(taskEvent.executeTime)) return false;
        if (!orderId.equals(taskEvent.orderId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventId.hashCode();
        result = 31 * result + orderId.hashCode();
        result = 31 * result + eventType.hashCode();
        result = 31 * result + executeTime.hashCode();
        return result;
    }
}
