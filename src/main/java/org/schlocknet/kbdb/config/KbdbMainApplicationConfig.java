package org.schlocknet.kbdb.config;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages =
{
  "org.schlocknet.kbdb.ws",
  "org.schlocknet.kbdb.services"
})
@Import(KbdbBaseConfigurator.class)
public class KbdbMainApplicationConfig extends WebMvcConfigurerAdapter
{

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private Environment env;

  public KbdbMainApplicationConfig()
  {
    // Do Nothing
  }

  // ===== Embedded Servlet Container Beans ==================================
  //<editor-fold defaultstate="collapsed" desc="dispatcherServlet">
  /**
   * Create the dispatcher servlet required for use with Spring MVC
   *
   * @return
   */
  @Bean
  public DispatcherServlet dispatcherServlet()
  {
    DispatcherServlet dispatcher = new DispatcherServlet();
    return dispatcher;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="servletContainer">
  /**
   * This creates and sets up properties for the embedded servlet container (in this case, Jetty)
   *
   * @return
   */
  @Bean
  public EmbeddedServletContainerFactory servletContainer()
  {
    JettyEmbeddedServletContainerFactory factory
      = new JettyEmbeddedServletContainerFactory();

    final String fleet = env.getProperty("fleet");
    System.out.println("Starting embedded servlet container for fleet: "
      + fleet);

    factory.setPort(8080);
    factory.setSessionTimeout(30, TimeUnit.MINUTES);
    factory.setContextPath("");

    return factory;
  }
  //</editor-fold>

  // ===== Web Server Configuration ==========================================
  //<editor-fold defaultstate="collapsed" desc="addResourceHandlers">
  /**
   * Add locations for static resources
   *
   * @param registry
   */
  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry)
  {
    logger.debug("Registering resource handlers...");
    registry.addResourceHandler("/favicon.ico")
      .addResourceLocations("/favicon.ico");
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="addInterceptors">
  /**
   * Register web service interceptor here
   *
   * @param registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry)
  {

    /*
        registry.addInterceptor(WebserviceAuthenticationInterceptor)
        .addPathPatterns(
        "/user/**"
        //"/sensor/**"
        );
     */
  }
  //</editor-fold>

  // ===== Main Entrypoint for SpringBoot ====================================
  public static void main(String[] args)
  {
    System.out.println("Running kbdb...");
    SpringApplication app = new SpringApplication(KbdbMainApplicationConfig.class);
    app.setWebEnvironment(true);
    app.run(args);
  }

}
