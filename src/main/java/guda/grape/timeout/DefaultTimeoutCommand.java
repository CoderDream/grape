package guda.grape.timeout;


import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by well on 15/3/17.
 */
public class DefaultTimeoutCommand implements Command {

    private Logger log = LoggerFactory.getLogger(DefaultTimeoutCommand.class);

    public void exec(final TaskEvent taskEvent,TaskFuture taskFuture) {
        if(log.isInfoEnabled()) {
            log.info("exec task:" + ReflectionToStringBuilder.toString(taskEvent));
        }

        try {
            if (taskEvent == null) {
                log.error("订单超时检测任务－执行失败 event is null");
                return;
            }
            //1,判断任务是否变更过超时时间，集群环境下，可能被另外一台服务器延长了超时时间
            //2,判断状态是否已经变更
        }finally{
            taskFuture.finish();
        }
    }


    public String getEventType() {
        return EventTypeEnum.TIME_OUT.name();
    }
}
