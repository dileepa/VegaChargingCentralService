package lk.vega.charger.centralservice.client.web.controller.user.chgOwner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/30/15.
 * Time on 5:49 PM
 */
@Controller
public class ChgOwnerController
{

    @RequestMapping(value = "/ChgOwnerSignUp", method = RequestMethod.GET)
    public ModelAndView loadChgOwnerSignUpView()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "user/chgOwner/chgOwnerSignUp" );
        return modelAndView;

    }

}
