package lk.vega.charger.centralservice.client.web.controller.user.chgOwner;

import lk.vega.charger.centralservice.client.web.dataLoader.user.ChgUserDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.GenderDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.TitelDataLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.user.ChgUser;
import lk.vega.charger.centralservice.client.web.domain.user.GenderBean;
import lk.vega.charger.centralservice.client.web.domain.user.TitleBean;
import lk.vega.charger.core.UserRoles;
import lk.vega.charger.userManagement.UserHandler;
import lk.vega.charger.util.ChgResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.wso2.carbon.um.ws.service.AddUser;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServicePortType;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.List;

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
        ChgUser chgUser = new ChgUser();
        modelAndView.getModel().put( "chgOwnerUser", chgUser );
        ChgResponse titlesRes = TitelDataLoader.loadAllTitleProperties();
        if( titlesRes.isSuccess() )
        {
            List<String> titlesList = (List) titlesRes.getReturnData();
            List titleBeanList = TitleBean.getBeanList( titlesList, DomainBeanImpl.USER_TITLE_BEAN_ID );
            modelAndView.getModel().put( "titleList",titleBeanList );
        }
        ChgResponse genderRes = GenderDataLoader.loadAllGenderProperties();
        if( genderRes.isSuccess() )
        {
            List<String> genderList = (List) genderRes.getReturnData();
            List genderBeanList = GenderBean.getBeanList( genderList, DomainBeanImpl.USER_GENDER_BEAN_ID );
            modelAndView.getModel().put( "genderList", genderBeanList );
        }
        return modelAndView;

    }

    @RequestMapping(value = "/saveNewChargeOwner", method = RequestMethod.POST)
    public ModelAndView saveNewLocation( @ModelAttribute("chgOwnerUser") ChgUser chgUser )
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "user/chgOwner/chgOwnerSignUpError" );
        ChgResponse chgResponse = null;
        try
        {

            chgResponse = UserHandler.connectToRemoteUserStoreManagerService();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        if( chgResponse.isSuccess() )
        {
            RemoteUserStoreManagerServicePortType remoteUserStoreManagerService = (RemoteUserStoreManagerServicePortType) chgResponse.getReturnData();
            try
            {
                StringBuilder profileNameBuilder = new StringBuilder();
                profileNameBuilder.append( chgUser.getFirstName() );
                profileNameBuilder.append( " " );
                profileNameBuilder.append( chgUser.getLastName() );
                chgUser.setProfileName( profileNameBuilder.toString() );
                chgUser.setUserRole( UserRoles.CHG_OWNER );
                AddUser newChgOwner = ChgUserDataLoader.createChargeUser( chgUser );
                remoteUserStoreManagerService.addUser( newChgOwner );
                modelAndView.setViewName( "user/chgOwner/chgOwnerSignUpSuccess" );
            }
            catch( SOAPFaultException e )
            {
                modelAndView.setViewName( "user/chgOwner/chgOwnerSignUpError" );
                modelAndView.getModel().put( "error",e.getMessage() );
                e.printStackTrace();
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }

        }

        return modelAndView;
    }

}
