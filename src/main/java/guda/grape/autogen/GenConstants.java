package guda.grape.autogen;

import java.io.File;

/**
 * Created by foodoon on 2014/6/27.
 */
public class GenConstants {

    public static String baseDir = System.getProperty("user.dir");

    public static String appDir = baseDir + File.separator + "app";

    public static String javaDir = "src"+ File.separator+"main"+ File.separator+"java";

    public static String resourceDir = "src"+ File.separator+"main"+ File.separator+"resources";

    public static String daoXML = "spring-dao.xml";

    public static String bizXML = "spring-biz.xml";

    public static String dataSourceXML = "spring-datasource.xml";

    public static String mybatisConfigXML = "sqlMapConfig.xml";

    //public static String parentPackage = "com" +File.separator + "foodoon";
}
