package lk.vega.charger.centralservice.client.web.domain.chargeStation;

import lk.vega.charger.centralservice.client.web.domain.GeneralProperty;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/29/15.
 * Time on 3:56 PM
 */
public class ChargeStationAvailabilityStatusBean extends GeneralProperty
{

    public static final String ACTIVE = "ACTIVE";
    public static final String BLOCK = "BLOCKED";

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
