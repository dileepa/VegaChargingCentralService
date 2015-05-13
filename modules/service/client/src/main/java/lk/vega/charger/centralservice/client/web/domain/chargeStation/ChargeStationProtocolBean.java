package lk.vega.charger.centralservice.client.web.domain.chargeStation;

import lk.vega.charger.centralservice.client.web.domain.GeneralProperty;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/13/15.
 * Time on 2:49 PM
 */
public class ChargeStationProtocolBean extends GeneralProperty
{
    public static final String PROTOCOL_J1772 = "J1772";
    public static final String PROTOCOL_CHARDEMO = "CHARDEMO";

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
