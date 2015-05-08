package lk.vega.charger.centralservice.client.web.domain.user;

import lk.vega.charger.centralservice.client.web.domain.GeneralProperty;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/8/15.
 * Time on 2:42 PM
 */
public class TitleBean extends GeneralProperty
{
    public static final String MISS = "Miss";
    public static final String MR = "Mr";
    public static final String DR = "Dr";

    @Override
    public void createBean( Object object )
    {
        setName( (String)object );
        setSelected( false );
    }

    @Override
    public void decodeBeanToReal( Object object )
    {
        super.decodeBeanToReal( object );
    }
}
