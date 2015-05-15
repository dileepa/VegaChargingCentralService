package lk.vega.charger.centralservice.client.web.controller.chgNetwork;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/15/15.
 * Time on 2:20 PM
 */
@Controller
public class ChargeNetworkController
{

    @RequestMapping(value = "/chargeNetwork/addChargeNetwork", method = RequestMethod.GET)
    public ModelAndView addChargeNetwork()
    {
        return null;
    }
}
