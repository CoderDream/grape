package guda.grape.autogen;

import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;

/**
 * Created by foodoon on 2014/6/26.
 */
public class GenDAOXML {

    private GenContext genContext;

    public GenDAOXML(GenContext genContext) {
        this.genContext = genContext;
    }


    public void gen() {
        createDAOXML();
        createMapConfig();
        createDatasourceXML();
    }

    public String createMapConfig() {


        String mapXml = genContext.getMybatisXmlFile();
        File file = new File(mapXml);
        if (!file.exists()) {
            Document document = MybatisConfigXml.createDocument();
            Element mapper = MybatisConfigXml.createRoot(document);
            MybatisConfigXml.appendVal(mapper, genContext.getRelativeDaoMapperXmlFile());
            SpringXml.write(mapXml, document);
            return mapXml;
        }
        Document document = SpringXml.loadXML(mapXml);
        if (!MybatisConfigXml.hasConfig(document, genContext.getRelativeDaoMapperXmlFile())) {
            Element mapper = MybatisConfigXml.getRoot(document);
            MybatisConfigXml.appendVal(mapper, genContext.getRelativeDaoMapperXmlFile());
            SpringXml.write(mapXml, document);
        }
        return mapXml;
    }

    public String createDAOXML() {
        String dalXML = genContext.getDaoXmlFile();
        File file = new File(dalXML);
        if (!file.exists()) {
            Document document = SpringXml.createDocument();
            Element springXmlHeader = SpringXml.createSpringXmlHeader(document);
            Element mapper = SpringXml.createBean(springXmlHeader, genContext.getDoNameLower() + "DAO", genContext.getParentPackageName()+".dao.impl."+genContext.getDoName()+"DAOImpl");

            SpringXml.appendRef(mapper, "sqlMapperR", "sqlMapClientRead");
            SpringXml.appendRef(mapper, "sqlMapperW", "sqlMapClientWrite");
            SpringXml.appendRef(mapper, "sqlMapClient", "sqlMapClientRead");
            SpringXml.write(dalXML, document);
            return dalXML;
        }
        Document document = SpringXml.loadXML(dalXML);
        if (!SpringXml.hasBean(document, genContext.getDoNameLower() + "DAO")) {
            Element mapper = SpringXml.createBean(SpringXml.getSpringRoot(document), genContext.getDoNameLower() + "DAO", genContext.getParentPackageName()+".dao.impl."+genContext.getDoName()+"DAOImpl");

            SpringXml.appendRef(mapper, "sqlMapperR", "sqlMapClientRead");
            SpringXml.appendRef(mapper, "sqlMapperW", "sqlMapClientWrite");
            SpringXml.appendRef(mapper, "sqlMapClient", "sqlMapClientRead");
            SpringXml.write(dalXML, document);
        }
        return dalXML;
    }

    public String createDatasourceXML() {

        String dalXML = genContext.getDataSourceXmlFile();
        File file = new File(dalXML);
        if (!file.exists()) {
            Document document = SpringXml.createDocument();
            Element springXmlHeader = SpringXml.createSpringXmlHeader(document);
            Element dataSource = SpringXml.createBean(springXmlHeader, "dataSourceRead", "com.alibaba.druid.pool.DruidDataSource");
            SpringXml.appendVal(dataSource, "driverClassName", "com.mysql.jdbc.Driver");
            SpringXml.appendVal(dataSource, "url", "${jdbc.url}");
            SpringXml.appendVal(dataSource, "username", "${jdbc.username}");
            SpringXml.appendVal(dataSource, "password", "${jdbc.password}");

            Element sqlSessionFactory = SpringXml.createBean(springXmlHeader, "sqlMapClientRead", "org.springframework.orm.ibatis.SqlMapClientFactoryBean");
            SpringXml.appendRef(sqlSessionFactory, "dataSource", "dataSourceRead");
            SpringXml.appendVal(sqlSessionFactory, "configLocation", "classpath:mybatis/sqlMapConfig.xml");

            Element writeDataSource = SpringXml.createBean(springXmlHeader, "dataSourceWrite", "com.alibaba.druid.pool.DruidDataSource");
            SpringXml.appendVal(writeDataSource, "driverClassName", "com.mysql.jdbc.Driver");
            SpringXml.appendVal(writeDataSource, "url", "${jdbc.url}");
            SpringXml.appendVal(writeDataSource, "username", "${jdbc.username}");
            SpringXml.appendVal(writeDataSource, "password", "${jdbc.password}");

            Element writeSqlSessionFactory = SpringXml.createBean(springXmlHeader, "sqlMapClientWrite", "org.springframework.orm.ibatis.SqlMapClientFactoryBean");
            SpringXml.appendRef(writeSqlSessionFactory, "dataSource", "dataSourceWrite");
            SpringXml.appendVal(writeSqlSessionFactory, "configLocation", "classpath:mybatis/sqlMapConfig.xml");

            SpringXml.write(dalXML, document);
            return dalXML;
        }
        Document document = SpringXml.loadXML(dalXML);
        boolean needUpdate = false;
        if (!SpringXml.hasBean(document, "dataSourceRead")) {
            Element dataSource = SpringXml.createBean(SpringXml.getSpringRoot(document), "dataSourceRead", "com.alibaba.druid.pool.DruidDataSource");
            SpringXml.appendVal(dataSource, "driverClassName", "com.mysql.jdbc.Driver");
            SpringXml.appendVal(dataSource, "url", "${jdbc.url}");
            SpringXml.appendVal(dataSource, "username", "${jdbc.username}");
            SpringXml.appendVal(dataSource, "password", "${jdbc.password}");
            needUpdate = true;
        }
        if (!SpringXml.hasBean(document, "sqlMapClientRead")) {
            Element sqlSessionFactory = SpringXml.createBean(SpringXml.getSpringRoot(document), "sqlMapClientRead", "org.springframework.orm.ibatis.SqlMapClientFactoryBean");
            SpringXml.appendRef(sqlSessionFactory, "dataSource", "dataSourceRead");
            SpringXml.appendVal(sqlSessionFactory, "configLocation", "classpath:mybatis/sqlMapConfig.xml");
            needUpdate = true;
        }
        if (needUpdate) {
            SpringXml.write(dalXML, document);
        }

        if (!SpringXml.hasBean(document, "dataSourceWrite")) {
            Element dataSource = SpringXml.createBean(SpringXml.getSpringRoot(document), "dataSourceWrite", "com.alibaba.druid.pool.DruidDataSource");
            SpringXml.appendVal(dataSource, "driverClassName", "com.mysql.jdbc.Driver");
            SpringXml.appendVal(dataSource, "url", "${jdbc.url}");
            SpringXml.appendVal(dataSource, "username", "${jdbc.username}");
            SpringXml.appendVal(dataSource, "password", "${jdbc.password}");
            needUpdate = true;
        }
        if (!SpringXml.hasBean(document, "sqlMapClientWrite")) {
            Element sqlSessionFactory = SpringXml.createBean(SpringXml.getSpringRoot(document), "sqlMapClientWrite", "org.springframework.orm.ibatis.SqlMapClientFactoryBean");
            SpringXml.appendRef(sqlSessionFactory, "dataSource", "dataSourceWrite");
            SpringXml.appendVal(sqlSessionFactory, "configLocation", "classpath:mybatis/sqlMapConfig.xml");
            needUpdate = true;
        }
        if (needUpdate) {
            SpringXml.write(dalXML, document);
        }

        return dalXML;
    }


}
