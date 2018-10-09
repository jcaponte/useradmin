/**
 * 
 */
package com.insoftar.useradmin.admin.config;

import javax.servlet.Filter;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;


import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.insoftar.useradmin.admin.security.PostAuthorizationFilter;
import com.insoftar.useradmin.common.services.JCLogger;

/**
 * @author Juan Carlos Aponte
 *
 */

@Configuration

public class WebConfig extends WebMvcConfigurerAdapter
{   
	
	protected final JCLogger logger = JCLogger.getLogger(getClass());
	
	@Value("${server.http.port}") private int serverHTTPPort;
	@Value("${useradmin.users.resource.handler}") private String userResourceHandler;
	@Value("${useradmin.users.resource.location}") private String userResourceLocation;
	@Value("${useradmin.users.resource.cache.period}") private int userResourceCachePeriod;
	

	
	@Autowired 
	private PostAuthorizationFilter postAuthorizationFilter;
	
	@Autowired
    private MessageSource messageSource;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

	    registry.addResourceHandler(userResourceHandler)
	            .addResourceLocations(userResourceLocation)
	            .setCachePeriod(userResourceCachePeriod)
	            .resourceChain(true)
	            .addResolver(new PathResourceResolver());
   
	}	

    @Override
    public Validator getValidator() {

        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);
        return factory;
    }
    	
	//http://stackoverflow.com/questions/25957879/filter-order-in-spring-boot
	@Bean
	public FilterRegistrationBean securityFilterChain(@Qualifier(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME) Filter securityFilter) {
	    FilterRegistrationBean registration = new FilterRegistrationBean(securityFilter);
	    registration.setOrder(Integer.MAX_VALUE - 1);
	    registration.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
	    return registration;
	}

	@Bean
	public FilterRegistrationBean PostAuthorizationFilterRegistrationBean() {

	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	    registrationBean.setFilter(postAuthorizationFilter);
	    registrationBean.setOrder(Integer.MAX_VALUE);
	    return registrationBean;
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry)
	{
		super.addViewControllers(registry);

        registry.addViewController("/login").setViewName("public/login");
		registry.addRedirectViewController("/", "/home");
		
	}
	
	@Bean
	public SpringSecurityDialect securityDialect() {

	    return new SpringSecurityDialect();
	}
	
	@Bean 
	public ClassLoaderTemplateResolver emailTemplateResolver(){ 

		ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver(); 
		emailTemplateResolver.setPrefix("email-templates/"); 
		emailTemplateResolver.setSuffix(".html"); 
		emailTemplateResolver.setTemplateMode("HTML5"); 
		emailTemplateResolver.setCharacterEncoding("UTF-8"); 
		emailTemplateResolver.setOrder(2);
		
		return emailTemplateResolver; 
	}
	
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("NONE");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
				
			}
		};

		//tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
		return tomcat;
	}

	
	private Connector initiateHttpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(serverHTTPPort);
		connector.setSecure(false);
		return connector;
	}



}
