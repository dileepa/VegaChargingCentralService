package lk.vega.charger.centralservice.client.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/22/15.
 * Time on 6:19 PM
 */
@Controller
public class ChargeStationDetailController
{
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ModelAndView index()
    {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("chargeStationDetail");
        return modelAndView;
    }


}

