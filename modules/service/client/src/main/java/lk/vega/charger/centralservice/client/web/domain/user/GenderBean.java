package lk.vega.charger.centralservice.client.web.domain.user;

import lk.vega.charger.centralservice.client.web.domain.GeneralProperty;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/8/15.
 * Time on 2:34 PM
 */
public class GenderBean extends GeneralProperty
{

    public static final String MALE = "MALE";
    public static final String FEMALE = "FEMALE";

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
