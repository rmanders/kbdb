package org.schlocknet.kbdb.config;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 *
 * @author Ryan
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "primaryEntityManagerFactory")
public class KbdbHibernateConfig implements TransactionManagementConfigurer
{

  /**
   * Private static logger
   */
  private static Logger LOGGER = LoggerFactory.getLogger(KbdbHibernateConfig.class);

  /**
   * Environmental properties
   */
  @Autowired
  private Environment env;

  /**
   * Primary data source for persistence
   */
  @Autowired
  private DataSource primaryDataSource;

  //<editor-fold defaultstate="collapsed" desc="primaryEntityManagerFactory">
  @Primary
  @Bean(name = "entityManagerFactory")
  public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory()
  {
    LOGGER.debug("Creating Local Entity Manager Factory Bean for Hibernate");

    checkPrimaryDataSource();

    final LocalContainerEntityManagerFactoryBean emf
      = new LocalContainerEntityManagerFactoryBean();

    final HibernateJpaVendorAdapter hva
      = new HibernateJpaVendorAdapter();

    final Boolean showSQL
      = env.getProperty("kbdb.database.relational.hibernate.showsql", Boolean.TYPE, Boolean.FALSE);

    final Boolean generateDdl
      = env.getProperty("kbdb.database.relational.hibernate.generateDdl", Boolean.TYPE, Boolean.FALSE);

    final String dialect
      = env.getProperty("kbdb.database.relational.hibernate.dialect", "org.hibernate.dialect.H2Dialect");

    final String persistenceUnitName
      = env.getProperty("kbdb.database.relational.persistenceUnitName", "kbdb");

    final String[] packageToScan
      =
      {
        "org.schlocknet.kbdb.model"
      };

    hva.setShowSql(showSQL);
    hva.setGenerateDdl(generateDdl);
    hva.setDatabasePlatform(dialect);

    emf.setJpaVendorAdapter(hva);
    emf.setDataSource(primaryDataSource);
    emf.setPackagesToScan(packageToScan);
    emf.setPersistenceUnitName(persistenceUnitName);

    return emf;
  }
  //</editor-fold>

  @Override
  public PlatformTransactionManager annotationDrivenTransactionManager()
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  private void checkPrimaryDataSource()
  {
    if (null == primaryDataSource)
    {
      throw new IllegalStateException("primary DataSource was null at the time of injection");
    }

  }

}
