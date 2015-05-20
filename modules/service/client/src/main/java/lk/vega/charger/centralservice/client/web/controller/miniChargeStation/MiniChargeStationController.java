package lk.vega.charger.centralservice.client.web.controller.miniChargeStation;

import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationLoader;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationAvailabilityStatusBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;
import lk.vega.charger.centralservice.client.web.domain.miniChargeStation.MobileChargeLocationBean;
import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.util.ChgResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/29/15.
 * Time on 11:52 AM
 */
@Controller
public class MiniChargeStationController
{

    /**
     * for json object.
     * @param chgStationID
     * @param amount
     * @return
     */
//    @RequestMapping(value = "/MiniChargeStationStartTransaction", method = RequestMethod.GET)
//    public @ResponseBody MiniChargeStationBean startTransaction
//            (
//                    @RequestParam(value = "chgStationID", required = false ) Integer chgStationID,
//                    @RequestParam(value = "amount", required = false ) String amount
//            )
//    {
//
//        MiniChargeStationBean miniChargeStationBean = new MiniChargeStationBean(chgStationID, amount);
//        miniChargeStationBean.createBean( null );
//        return miniChargeStationBean;
////        ModelAndView modelAndView = new ModelAndView();
////        modelAndView.setViewName( "miniChargeStation/miniChargerStartRes" );
////        modelAndView.getModel().put( "x", 18 );
////        modelAndView.getModel().put( "y", 45 );
////        return modelAndView;
//
//
//    }

    @RequestMapping(value = "/MiniChargeStationStartTransaction", method = RequestMethod.GET)
    public @ResponseBody String startTransaction
            (
                    @RequestParam(value = "chgStationID", required = false ) Integer chgStationID,
                    @RequestParam(value = "amount", required = false ) String amount
            )
    {
        return "3600:158138: vega123 start";


    }

    @RequestMapping(value = "/getActiveChargingPointLocations", method = RequestMethod.GET)
    public @ResponseBody List<MobileChargeLocationBean> getActiveChargingPointLocations()
    {
        ChgResponse chgResponse = ChargeStationLoader.loadAllChargePoints();
        List<MobileChargeLocationBean> mobileChargeLocationBeans = new ArrayList<MobileChargeLocationBean>(  );
        if (chgResponse.isSuccess())
        {
            List<ChargePoint> chargePointList = (List<ChargePoint>)chgResponse.getReturnData();
            for ( ChargePoint chargePoint : chargePointList)
            {
                if ( ChargeStationAvailabilityStatusBean.ACTIVE.equals( chargePoint.getChargePointAvailabilityStatus() ))
                {
                    ChargeStationBean chargeStationBean = new ChargeStationBean();
                    chargeStationBean.createBean( chargePoint );
                    MobileChargeLocationBean mobileChargeLocationBean = new MobileChargeLocationBean();
                    mobileChargeLocationBean.createBean( chargeStationBean );
                    mobileChargeLocationBeans.add( mobileChargeLocationBean );
                }
            }
        }
        return mobileChargeLocationBeans;
    }

}
