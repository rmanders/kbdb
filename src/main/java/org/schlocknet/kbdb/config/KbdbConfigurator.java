package org.schlocknet.kbdb.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages={"org.schlocknet.kbdb.ws"})
@PropertySource("org/schlocknet/config/${FLEET:default}/database.properties")
public class KbdbConfigurator extends WebMvcConfigurerAdapter {
   
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private Environment env;
    
    @Autowired
    HikariDataSource relationalDataSource;
    
    public KbdbConfigurator() {
        // Do Nothing
    }
    
    //<editor-fold defaultstate="collapsed" desc="relationalDataSource">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="buildHsqlDataSource">
    public org.hsqldb.jdbc.JDBCDataSource buildHsqlDataSource() {
        org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
        ds.setDatabase("kbdb");
        return ds;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="dispatcherServlet">
    /**
     * Create the dispatcher servlet required for use with Spring MVC
     * @return 
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcher = new DispatcherServlet();
        return dispatcher;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="servletContainer">
    /**
     * This creates and sets up properties for the embedded servlet container
     * (in this case, Jetty)
     * 
     * @return 
     */
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        JettyEmbeddedServletContainerFactory factory =
                new JettyEmbeddedServletContainerFactory();
        
        final String fleet = env.getProperty("fleet");
        System.out.println("Starting embedded servlet container for fleet: "
                + fleet);
        
        factory.setPort(8080);
        factory.setSessionTimeout(30, TimeUnit.MINUTES);
        factory.setContextPath("");
        
        return factory;
    }
    //</editor-fold>
    
    public static void main(String[] args) {
        System.out.println("Running kbdb...");
        SpringApplication app = new SpringApplication(KbdbConfigurator.class);
        app.setWebEnvironment(true);
        app.run(args);
    }
       
}
