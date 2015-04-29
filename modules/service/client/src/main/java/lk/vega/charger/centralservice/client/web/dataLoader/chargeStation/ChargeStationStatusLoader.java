package lk.vega.charger.centralservice.client.web.dataLoader.chargeStation;

import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.util.ChgResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/29/15.
 * Time on 4:09 PM
 */
public class ChargeStationStatusLoader
{
    public static ChgResponse loadAllChargePointStatus()
    {
        List<String> chargePointStatusList = new ArrayList<String>(  );
        chargePointStatusList.add( ChargePoint.CHG_POINT_ACTIVE );
        chargePointStatusList.add( ChargePoint.CHG_POINT_BLOCKED );
        return new ChgResponse( ChgResponse.SUCCESS,"Load Charge Station Status Successfully",chargePointStatusList  );
    }
}
