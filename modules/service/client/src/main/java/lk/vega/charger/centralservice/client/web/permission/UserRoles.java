package lk.vega.charger.centralservice.client.web.permission;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/5/15.
 * Time on 1:00 PM
 */
public class UserRoles
{


    public static final String SKIP_ROLE = "Internal/everyone";

    public static final String CHG_CUSTOMER = "CHG_CUSTOMER";

    public static final String CHG_OWNER = "CHG_OWNER";

    public static final String CHG_ADMIN = "CHG_ADMIN";

    public static boolean isRoleExist(List<GrantedAuthority> grantedAuthorityList, String roleName )
    {
        for( GrantedAuthority grantedAuthority : grantedAuthorityList )
        {
            if ( roleName.equals( grantedAuthority.getAuthority() ))
            {
                return true;
            }
        }
        return false;
    }


}
