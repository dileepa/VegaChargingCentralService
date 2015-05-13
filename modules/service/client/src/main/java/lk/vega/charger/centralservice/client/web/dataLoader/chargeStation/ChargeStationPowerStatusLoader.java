package lk.vega.charger.centralservice.client.web.dataLoader.chargeStation;

import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationPowerStatusBean;
import lk.vega.charger.util.ChgResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/13/15.
 * Time on 2:58 PM
 */
public class ChargeStationPowerStatusLoader
{
    public static ChgResponse loadAllChargePointPowerStatus()
    {
        List<String> chargePointPowerStatusList = new ArrayList<String>(  );
        chargePointPowerStatusList.add( ChargeStationPowerStatusBean.POWER_OFF );
        chargePointPowerStatusList.add( ChargeStationPowerStatusBean.POWER_ON );
        return new ChgResponse( ChgResponse.SUCCESS,"Load Charge Station Power Status Successfully",chargePointPowerStatusList  );
    }

}
