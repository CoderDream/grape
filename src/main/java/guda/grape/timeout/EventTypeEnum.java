package guda.grape.timeout;

/**
 * Created by well on 15/6/10.
 */
public enum EventTypeEnum {

    TIME_OUT("超时事件", 1),
            ;

    private String desc;

    private Integer value;

    EventTypeEnum(String desc, Integer status) {
        this.desc = desc;
        this.value = status;
    }

    public String getDesc() {
        return desc;
    }


    public Integer getValue() {
        return value;
    }
}
