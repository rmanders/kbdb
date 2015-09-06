package org.schlocknet.kbdb.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import org.schlocknet.kbdb.dao.LargeObjectStore;
import org.schlocknet.kbdb.dao.LargeObjectStoreFileSystemImpl;
import org.schlocknet.kbdb.dao.UserDao;
import org.schlocknet.kbdb.dao.UserDaoHibernateImpl;
import org.schlocknet.kbdb.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 *
 * @author Ryan
 * 
 * Configuration class for initializing database connections etc
 */
@Configuration
@PropertySource({
    "org/schlocknet/config/${fleet:default}/database.properties", 
    "org/schlocknet/config/${fleet:default}/app.properties"})
public class KbdbBaseConfigurator implements TransactionManagementConfigurer {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private Environment env;
    
    @Autowired
    HikariDataSource relationalDataSource;
    
    @Autowired
    LocalSessionFactoryBean sessionFactory;
    
    @Autowired
    HibernateTransactionManager transactionManager;

    // ===== Data Source Beans =================================================
    
    //<editor-fold defaultstate="collapsed" desc="relationalDataSource">
    @Bean
    public HikariDataSource relationalDataSource() {
        logger.debug("Creating relationalDatasource bean");
        HikariConfig cfg = new HikariConfig();
        
        final String dataSourceClassName = 
                env.getProperty("dataSource.dataSourceClass");
        final String diverClassName =   
                env.getProperty("dataSource.driverClass");
        
        
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
    
    //<editor-fold defaultstate="collapsed" desc="sessionFactory">
    @Bean
    @DependsOn("relationalDataSource")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean session = new LocalSessionFactoryBean();
        Properties props = new Properties();
        
        props.setProperty("hibernate.dialect",
                env.getProperty(
                        "hibernate.dialect",
                        "org.hibernate.dialect.MySQLDialect"
                )
        );
        props.setProperty("hibernate.show_sql;", "true");
        //props.setProperty("hibernate.c3p0.timeout", "600");
        //props.setProperty("hibernate.c3p0.idle_test_period", "5000");
        
        //session.setPackagesToScan("org.schlocknet.kbdb.entity");
        session.setHibernateProperties(props);
        session.setMappingResources(
                "org/schlocknet/hibernate/UserModel.hbm.xml"
                ,"org/schlocknet/hibernate/ManufacturerModel.hbm.xml"
                ,"org/schlocknet/hibernate/ManufacturerModelModel.hbm.xml"
        );
        session.setDataSource(relationalDataSource);
        return session;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="transactionManager">
    /**
     * Transaction manager though hibernate
     * @return 
     */
    @Bean(name="transactionManager")
    @DependsOn("sessionFactory")
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager txm = new HibernateTransactionManager();
        txm.setSessionFactory(sessionFactory.getObject());
        return txm;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="annotationDrivenTransactionManager">
    /**
     * This allows @Transactional annotations to be used in data access objects
     * @return 
     */
    @Override
    @DependsOn("transactionManager")
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager;
    }
    //</editor-fold>

    // ===== Data Access Beans =================================================
    
    //<editor-fold defaultstate="collapsed" desc="userDao">
    @Bean
    public UserDao userDao() {
        return new UserDaoHibernateImpl();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="largeObjectStore">
    @Bean
    public LargeObjectStore largeObjectStore() {
        final String storeType = env.getProperty("app.objectStore.type",
                "file");
        final String basePath = env.getProperty("app.objectStore.basePath",
                System.getProperty("java.io.tmpdir","/tmp"));
        // TODO: add support for different storeTypes with the default being
        // file store
        LargeObjectStoreFileSystemImpl los =
                new LargeObjectStoreFileSystemImpl(basePath);
        return los;
    }
    //</editor-fold>
    
    // ===== Service Beans =====================================================
    
    //<editor-fold defaultstate="collapsed" desc="securityService">
    @Bean
    public SecurityService securityService()
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        
        String secretKey;
        if (System.getProperty("app.secretKey", null) != null) {
            secretKey = System.getProperty("app.secretKey");
        } else {
            secretKey = env.getProperty("app.secretKey");
        }
        if (secretKey == null) {
            logger.error("Could not find the secretKey needed for the "
                    + "SecurityService to start. The secretKey must be defined "
                    + "in app.properties file or using the "
                    + "-Dapp.secretKey=<key> option");
            throw new IllegalStateException("Could not find secretKey");
        }
        return new SecurityService(secretKey);
    }
    //</editor-fold>
    
}
