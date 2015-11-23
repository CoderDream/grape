package guda.grape.mvc.security;

/**
 * Created by foodoon on 2014/12/20.
 */
public class AppContexHolder {

    private static ThreadLocal<AppContext> contextHolder = new ThreadLocal<AppContext>();

    private static AppContext nullContext = new AppContext();

    public static void setContext(AppContext context) {
         contextHolder.set(context);
    }


    public static AppContext getContext() {
        AppContext appContext = contextHolder.get();
        if(appContext == null){
            return nullContext;
        }
        return appContext;
    }

    public static void clear() {
        contextHolder.remove();
    }

}
