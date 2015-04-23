/**
 * 
 */
package lk.vega.charger.centralservice.client.web.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author Lasitha
 *
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
	/*
	 * (non-Javadoc)
	 * @see org.springframework.it.codegen.tbx.web.servlet.support. AbstractAnnotationConfigDispatcherServletInitializer #getRootConfigClasses()
	 */
	@Override
	protected Class<?>[] getRootConfigClasses()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.it.codegen.tbx.web.servlet.support. AbstractAnnotationConfigDispatcherServletInitializer #getServletConfigClasses()
	 */
	@Override
	protected Class<?>[] getServletConfigClasses()
	{
		return new Class[] { WebMvcConfig.class };
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.it.codegen.tbx.web.servlet.support.AbstractDispatcherServletInitializer #getServletMappings()
	 */
	@Override
	protected String[] getServletMappings()
	{
		return new String[]{"/"};
	}
}
