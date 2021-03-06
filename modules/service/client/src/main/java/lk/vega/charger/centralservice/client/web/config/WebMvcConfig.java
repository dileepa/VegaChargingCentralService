/**
 * 
 */
package lk.vega.charger.centralservice.client.web.config;

import lk.vega.charger.util.CoreController;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Lasitha
 *
 */
@ComponentScan(basePackages = "lk.vega.charger.centralservice.client.web")
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter
{

	@Override
	public void addResourceHandlers( ResourceHandlerRegistry registry )
	{
		registry.addResourceHandler( "/**" ).addResourceLocations( "/" );
	}

	@Bean
	public ViewResolver viewResolver()
	{
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setCacheable( false );
		templateResolver.setPrefix( "/WEB-INF/html_new/" );
		templateResolver.setSuffix( ".html" );
		templateResolver.setTemplateMode( "HTML5" );


		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver( templateResolver );
        templateEngine.setMessageResolver( messageResolver() );

        Set<IDialect> dialects = new HashSet<IDialect>();
        dialects.add( new SpringSecurityDialect() );
        templateEngine.setAdditionalDialects( dialects );


        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setCharacterEncoding( "UTF-8" );
		viewResolver.setOrder( 1 );
		viewResolver.setTemplateEngine( templateEngine );

		return viewResolver;
	}

    @Bean
    public MessageSource messageSource()
    {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("/WEB-INF/i18n/messages");
        messageSource.setDefaultEncoding( "UTF-8" );
        return messageSource;
    }

    @Bean
    public IMessageResolver messageResolver() {
        SpringMessageResolver messageResolver = new SpringMessageResolver();
        messageResolver.setMessageSource(messageSource());
        return messageResolver;
    }

    @Bean
    public Object initConfigurations()
    {
        try
        {
            CoreController.init();
            CoreController.loadServiceConfigurations();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        return null;
    }
}

