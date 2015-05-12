package lk.vega.charger.centralservice.client.web.config;

import lk.vega.charger.centralservice.client.web.provider.UserAuthenticationProvider;
import lk.vega.charger.centralservice.client.web.permission.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/11/15.
 * Time on 11:42 AM
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired UserAuthenticationProvider authenticationProvider;

    @Autowired
    public void configureGlobal( AuthenticationManagerBuilder auth )
    {
        try
        {
            auth.authenticationProvider( authenticationProvider );
           // auth.inMemoryAuthentication().withUser( "user" ).password( "password" ).roles( "USER" );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception
    {

      http
              .csrf().disable()
              .exceptionHandling().accessDeniedPage( "/403" )
              .and()
              .authorizeRequests()


                      /**
                       * Define Here All Charging Admin Permissions.
                       */
              .antMatchers( "/AllChargeStations" ).hasAuthority( UserRoles.CHG_ADMIN )
              .antMatchers( "/chargeStation**" ).hasAuthority( UserRoles.CHG_ADMIN )
              .antMatchers( "/saveNewChargeStation" ).hasAuthority( UserRoles.CHG_ADMIN )
              .antMatchers( "/saveExistingChargeStation" ).hasAuthority( UserRoles.CHG_ADMIN )

              /**
               * Define Here All Charging Admin Permissions.
               */



              .and()
              .formLogin().loginPage( "/Login" ).loginProcessingUrl( "/AuthenticateUser" ).defaultSuccessUrl( "/LoginSuccess" ).failureUrl( "/LoginError" ).usernameParameter( "username" ).passwordParameter( "password" )
              .and()
              .logout().logoutRequestMatcher( new AntPathRequestMatcher( "/logout" ) ).logoutSuccessUrl( "/login?logout=1" ).deleteCookies( "JSESSIONID" ).invalidateHttpSession( true )
              .and()
              .sessionManagement().invalidSessionUrl( "/" ).maximumSessions( 2 );
    }

    @Bean
    public UserAuthenticationProvider authenticationProvider()
    {
        return new UserAuthenticationProvider();
    }
}
