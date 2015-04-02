package org.schlocknet.kbdb.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("org/schlocknet/config/${FLEET:default}/database.properties")
public class KbdbConfigurator {
   
    final static Logger logger = LoggerFactory.getLogger(KbdbConfigurator.class);
    
    @Autowired
    private Environment env;
    
    @Autowired
    HikariDataSource relationalDataSource;
    
    public KbdbConfigurator() {
        // Do Nothing
    }
    
    @Bean
    public HikariDataSource relationalDataSource() {
        logger.debug("Creating relationalDatasource bean");
        HikariConfig cfg = new HikariConfig();       
        
        final String dataSourceClassName = env.getProperty("dataSource.dataSourceClass");
        final String diverClassName = env.getProperty("dataSource.driverClass");
        
        
        cfg.setJdbcUrl(env.getProperty("dataSource.jdbcUrl"));
        //org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
        //cfg.setDataSourceClassName(env.getProperty("dataSource.className"));
        //cfg.setCatalog(env.getProperty("dataSource.database", "kbdb"));
        cfg.setUsername(env.getProperty("dataSource.username", "none"));
        cfg.setPassword(env.getProperty("dataSource.password", "none"));
        //cfg.setPoolName(env.getProperty("cp.poolName","kbdbConnectionPool"));
        logger.debug("relationalDataSource config: {}", cfg.toString());
        try {
            HikariDataSource ds = new HikariDataSource(cfg);           
            return ds;
        } catch (Exception ex) {
            System.out.println("EXCEPTION IN HIKARI POOL INIT");
            ex.printStackTrace(System.out);
            throw new RuntimeException(ex);
        }
        //return null;
    }
    
    public org.hsqldb.jdbc.JDBCDataSource buildHsqlDataSource() {
        org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
        ds.setDatabase("kbdb");
        return ds;
    }
       
}
