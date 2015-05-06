package lk.vega.charger.centralservice.client.web.controller.user.chgOwner;

import lk.vega.charger.centralservice.client.web.domain.user.chgOwner.ChgOwnerBean;
import lk.vega.charger.core.ChgUser;
import lk.vega.charger.userManagement.UserHandler;
import lk.vega.charger.util.ChgResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        ChgOwnerBean chgOwnerBean = new ChgOwnerBean();
        ChgUser chgUser = new ChgUser();
        chgUser.init();
        chgOwnerBean.createBean( chgUser );
        modelAndView.getModel().put( "chgOwnerUser",chgOwnerBean );
        return modelAndView;

    }

    @RequestMapping(value = "/saveNewChargeOwner", method = RequestMethod.POST)
    public ModelAndView saveNewLocation(@ModelAttribute("chgOwnerUser" ) ChgOwnerBean chgOwnerBean )
    {
        ChgResponse chgResponse = null;
        try
        {

            chgResponse = UserHandler.connectToRemoteUserStoreManagerService();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
//        if (chgResponse.isSuccess())
//        {
//            UserAdminPortType userAdminService = (UserAdminPortType)chgResponse.getReturnData();
//            try
//            {
//                AddUser newUser = new AddUser();
//                newUser.setUserName( chgOwnerBean.getUserName() );
//                userAdminService.addUser( chgOwnerBean.getUserName(), chgOwnerBean.getPassword(),new String[]{UserRoles.CHG_OWNER},null,null );
//            }
//            catch( RemoteException e )
//            {
//                e.printStackTrace();
//            }
//            catch( UserAdminUserAdminException e )
//            {
//                e.printStackTrace();
//            }
//            catch( Exception e )
//            {
//                e.printStackTrace();
//            }
//
//        }

        return new ModelAndView(  );
    }

}
