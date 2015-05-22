package lk.vega.charger.centralservice.client.web.domain.user;

import lk.vega.charger.centralservice.client.web.domain.GeneralProperty;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/22/15.
 * Time on 12:14 PM
 */
public class UserStatusBean extends GeneralProperty
{

    @Override
    public void createBean( Object object )
    {
        setName( (String) object );
        setSelected( false );
    }

    @Override
    public void decodeBeanToReal( Object object )
    {
        super.decodeBeanToReal( object );
    }
}
