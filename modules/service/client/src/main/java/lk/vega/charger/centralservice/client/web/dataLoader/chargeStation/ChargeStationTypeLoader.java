package lk.vega.charger.centralservice.client.web.dataLoader.chargeStation;

import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationTypeBean;
import lk.vega.charger.util.ChgResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/13/15.
 * Time on 2:59 PM
 */
public class ChargeStationTypeLoader
{
    public static ChgResponse loadAllChargePointTypes()
    {
        List<String> chargePointTypeList = new ArrayList<String>(  );
        chargePointTypeList.add( ChargeStationTypeBean.LEVEL2 );
        chargePointTypeList.add( ChargeStationTypeBean.LEVEL3 );
        return new ChgResponse( ChgResponse.SUCCESS,"Load Charge Station Types Successfully",chargePointTypeList  );
    }

}
