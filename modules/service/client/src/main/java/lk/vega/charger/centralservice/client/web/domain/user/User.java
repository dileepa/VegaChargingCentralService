package lk.vega.charger.centralservice.client.web.domain.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/11/15.
 * Time on 10:58 AM
 */
public class User extends org.springframework.security.core.userdetails.User
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param username
     * @param password
     * @param enabled
     * @param accountNonExpired
     * @param credentialsNonExpired
     * @param accountNonLocked
     * @param authorities
     */
    public User( String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities )
    {
        super( username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities );
    }

    /**
     * @param username
     * @param password
     * @param authorities
     */
    public User( String username, String password, Collection<? extends GrantedAuthority> authorities )
    {
        super( username, password, authorities );
    }
}
