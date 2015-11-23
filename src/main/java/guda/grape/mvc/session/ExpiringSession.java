package guda.grape.mvc.session;


public interface ExpiringSession extends Session {


    long getCreationTime();


    long getLastAccessedTime();


    void setMaxInactiveIntervalInSeconds(int interval);


    int getMaxInactiveIntervalInSeconds();

    boolean isExpired();
}
