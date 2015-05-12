package lk.vega.charger.centralservice.client.web.controller.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/12/15.
 * Time on 11:43 AM
 */
@Controller
public class ErrorController
{
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView error403View()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "errors/accessDenied403" );
        return modelAndView;
    }
}
