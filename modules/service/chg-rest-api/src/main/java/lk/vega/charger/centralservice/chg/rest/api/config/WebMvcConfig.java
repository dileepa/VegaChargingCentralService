/**
 * 
 */
package lk.vega.charger.centralservice.chg.rest.api.config;

import lk.vega.charger.util.CoreController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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

