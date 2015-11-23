package guda.grape.mvc.log;

import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;

/**
 * Created by well on 15/10/15.
 */
public class LogbackWebConfigurer {

    public static final String CONFIG_LOCATION_PARAM = "logbackConfigLocation";


    public static final String REFRESH_INTERVAL_PARAM = "logbackRefreshInterval";

    public static final String EXPOSE_WEB_APP_ROOT_PARAM = "logbackExposeWebAppRoot";


    public static void initLogging(ServletContext servletContext) {
        // Expose the web app root system property.
        if (exposeWebAppRoot(servletContext)) {
            WebUtils.setWebAppRootSystemProperty(servletContext);
        }
        // Only perform custom logback initialization in case of a config file.
        String location = servletContext
                .getInitParameter(CONFIG_LOCATION_PARAM);
        if (location != null) {
            // Perform actual logback initialization; else rely on logback's
            // default initialization.
            try {
                // Return a URL (e.g. "classpath:" or "file:") as-is;
                // consider a plain file path as relative to the web application
                // root directory.
                if (!ResourceUtils.isUrl(location)) {
                    // Resolve system property placeholders before resolving
                    // real path.
                    location = SystemPropertyUtils
                            .resolvePlaceholders(location);
                    location = WebUtils.getRealPath(servletContext, location);
                }

                // Write log message to server log.
                servletContext.log("Initializing logback from [" + location
                        + "]");

                // Initialize without refresh check, i.e. without logback's
                // watchdog thread.
                LogbackConfigurer.initLogging(location);

            } catch (FileNotFoundException ex) {
                throw new IllegalArgumentException(
                        "Invalid 'logbackConfigLocation' parameter: "
                                + ex.getMessage());
            }
        }
    }


    public static void shutdownLogging(ServletContext servletContext) {
        servletContext.log("Shutting down logback");
        try {
            LogbackConfigurer.shutdownLogging();
        } finally {
            // Remove the web app root system property.
            if (exposeWebAppRoot(servletContext)) {
                WebUtils.removeWebAppRootSystemProperty(servletContext);
            }
        }
    }


    private static boolean exposeWebAppRoot(ServletContext servletContext) {
        String exposeWebAppRootParam = servletContext
                .getInitParameter(EXPOSE_WEB_APP_ROOT_PARAM);
        return (exposeWebAppRootParam == null || Boolean
                .valueOf(exposeWebAppRootParam));
    }
}
