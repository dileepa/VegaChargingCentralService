package lk.vega.charger.centralservice.chg.rest.api.provider;

import lk.vega.charger.centralservice.client.web.domain.user.User;
import lk.vega.charger.centralservice.client.web.permission.UserRoles;
import lk.vega.charger.userManagement.UserHandler;
import lk.vega.charger.util.ChgResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServicePortType;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/11/15.
 * Time on 1:38 PM
 */
public class UserAuthenticationTokenProvider implements AuthenticationProvider
{

    public static final String INVALID_USER_TOKEN = "Invalid User Token";

    /*
     * (non-Javadoc)
     * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
     */
    @Override
    public Authentication authenticate( Authentication authentication ) throws AuthenticationException
    {
        String error = "";
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

        String username = String.valueOf( auth.getPrincipal() );
        String password = String.valueOf( auth.getCredentials() );
        ChgResponse chgResponse = null;
        try
        {
            chgResponse = UserHandler.connectToRemoteUserStoreManagerService();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        if( chgResponse.isSuccess() )
        {
            RemoteUserStoreManagerServicePortType remoteUserStoreManagerService = (RemoteUserStoreManagerServicePortType) chgResponse.getReturnData();
            try
            {
                boolean isAuthenticate = remoteUserStoreManagerService.authenticate( username, password );
                if( isAuthenticate )
                {
                    List<String> roleListForUserFromIS = remoteUserStoreManagerService.getRoleListOfUser( username );
                    List<String> roleListForUser = removeUnwatedRoles( roleListForUserFromIS );
                    List<GrantedAuthority> grantedAuthorityList = buildUserAuthority( roleListForUser );
                    User user = new User(username,password,grantedAuthorityList);
                    return new UsernamePasswordAuthenticationToken( user, user.getPassword(), user.getAuthorities() );

                }
                else
                {
                    error = INVALID_USER_TOKEN;
                }
            }
            catch( SOAPFaultException e )
            {
                error = e.getMessage();
                e.printStackTrace();
            }
            catch( Exception e )
            {
                error = e.getMessage();
                e.printStackTrace();
            }


        }
        throw new BadCredentialsException( error );
    }

    private List<String> removeUnwatedRoles( List<String> roleListForUser )
    {
        List<String> finalRoles = new ArrayList<String>();
        for( String role : roleListForUser )
        {
            if( !UserRoles.SKIP_ROLE.equals( role ) )
            {
                finalRoles.add( role );
            }
        }
        return finalRoles;
    }

    private List<GrantedAuthority> buildUserAuthority( List<String> roleListForUser )
    {
        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
        for( String role : roleListForUser )
        {
            result.add( new SimpleGrantedAuthority( role ) );
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
     */
    @Override
    public boolean supports( Class<?> authentication )
    {
        return true;
    }


}
