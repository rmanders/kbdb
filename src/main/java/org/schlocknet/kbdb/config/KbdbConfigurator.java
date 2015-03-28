package org.schlocknet.kbdb.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
        cfg.setDataSourceClassName(env.getProperty("dataSource.className"));
        cfg.setCatalog(env.getProperty("dataSource.database", "kbdb"));
        cfg.setUsername(env.getProperty("dataSource.username", "none"));
        cfg.setPassword(env.getProperty("dataSource.password", "none"));
        cfg.setPoolName(env.getProperty("cp.poolName","kbdbConnectionPo`ol"));
        logger.debug("relationalDataSource config: {}", cfg.toString());
        HikariDataSource ds = new HikariDataSource(cfg);
        return ds;
    }
       
}
