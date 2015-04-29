package lk.vega.charger.centralservice.client.web.domain.miniChargeStation;

import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/29/15.
 * Time on 1:46 PM
 */
public class MiniChargeStationBean extends DomainBeanImpl
{
    public int x;
    public int y;
    public int chargeId;
    public String amount;

    public MiniChargeStationBean( int chargeId, String amount )
    {
        this.chargeId = chargeId;
        this.amount = amount;
    }

    @Override
    public void createBean( Object object )
    {
        super.createBean( object );
        x=5;
        y=9;
    }


    @Override
    public void decodeBeanToReal( Object object )
    {
        super.decodeBeanToReal( object );
    }


}
