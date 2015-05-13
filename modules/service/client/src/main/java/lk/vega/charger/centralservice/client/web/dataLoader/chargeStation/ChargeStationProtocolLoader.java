package lk.vega.charger.centralservice.client.web.dataLoader.chargeStation;

import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationProtocolBean;
import lk.vega.charger.util.ChgResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/13/15.
 * Time on 2:59 PM
 */
public class ChargeStationProtocolLoader
{
    public static ChgResponse loadAllChargePointProtocols()
    {
        List<String> chargePointProtocolList = new ArrayList<String>(  );
        chargePointProtocolList.add( ChargeStationProtocolBean.PROTOCOL_CHARDEMO );
        chargePointProtocolList.add( ChargeStationProtocolBean.PROTOCOL_J1772 );
        return new ChgResponse( ChgResponse.SUCCESS,"Load Charge Station Protocols Successfully",chargePointProtocolList  );
    }

}
