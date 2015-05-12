package lk.vega.charger.centralservice.client.web.controller.miniChargeStation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
