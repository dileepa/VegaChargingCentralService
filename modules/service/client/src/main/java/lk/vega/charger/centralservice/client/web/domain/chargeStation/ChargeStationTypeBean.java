package lk.vega.charger.centralservice.client.web.domain.chargeStation;

import lk.vega.charger.centralservice.client.web.domain.GeneralProperty;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/13/15.
 * Time on 2:48 PM
 */
public class ChargeStationTypeBean extends GeneralProperty
{
    public static final String LEVEL2 = "L2";
    public static final String LEVEL3 = "L3";

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
