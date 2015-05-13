package lk.vega.charger.centralservice.client.web.dataLoader.chargeStation;

import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationAvailabilityStatusBean;
import lk.vega.charger.util.ChgResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/29/15.
 * Time on 4:09 PM
 */
public class ChargeStationAvailabilityStatusLoader
{
    public static ChgResponse loadAllChargePointStatus()
    {
        List<String> chargePointStatusList = new ArrayList<String>(  );
        chargePointStatusList.add( ChargeStationAvailabilityStatusBean.ACTIVE );
        chargePointStatusList.add( ChargeStationAvailabilityStatusBean.BLOCK );
        return new ChgResponse( ChgResponse.SUCCESS,"Load Charge Station Status Successfully",chargePointStatusList  );
    }
}
