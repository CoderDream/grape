package guda.grape.autogen.common;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


public class BaseDAO extends SqlMapClientDaoSupport {
    private SqlMapClient sqlMapperR; //读
    private  SqlMapClient sqlMapperW; //写

    public SqlMapClient getSqlMapperR() {
        return sqlMapperR;
    }

    public void setSqlMapperR(SqlMapClient sqlMapperR) {
        this.sqlMapperR = sqlMapperR;
    }

    public SqlMapClient getSqlMapperW() {
        return sqlMapperW;
    }

    public void setSqlMapperW(SqlMapClient sqlMapperW) {
        this.sqlMapperW = sqlMapperW;
    }
}
