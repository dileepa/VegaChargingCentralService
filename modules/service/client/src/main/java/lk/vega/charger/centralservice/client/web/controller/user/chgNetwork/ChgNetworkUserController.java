package lk.vega.charger.centralservice.client.web.controller.user.chgNetwork;

import lk.vega.charger.centralservice.client.web.dataLoader.user.ChgUserDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.GenderDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.TitelDataLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.user.ChgUserBean;
import lk.vega.charger.centralservice.client.web.domain.user.GenderBean;
import lk.vega.charger.centralservice.client.web.domain.user.TitleBean;
import lk.vega.charger.centralservice.client.web.permission.UserRoles;
import lk.vega.charger.userManagement.UserHandler;
import lk.vega.charger.util.ChgResponse;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.wso2.carbon.um.ws.service.AddUser;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServicePortType;

import javax.validation.Valid;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/15/15.
 * Time on 2:05 PM
 */
@Controller
public class ChgNetworkUserController
{
    @RequestMapping(value = "/ChgNetworkUserSignUp", method = RequestMethod.GET)
    public ModelAndView loadChgNetworkSignUpView()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "user/chgNetwork/chgNetworkSignUp" );
        ChgUserBean chgUserBean = new ChgUserBean();
        modelAndView.getModel().put( "chgNetworkUser", chgUserBean );
        ChgResponse titlesRes = TitelDataLoader.loadAllTitleProperties();
        if( titlesRes.isSuccess() )
        {
            List<String> titlesList = (List) titlesRes.getReturnData();
            List titleBeanList = TitleBean.getBeanList( titlesList, DomainBeanImpl.USER_TITLE_BEAN_ID );
            modelAndView.getModel().put( "titleList", titleBeanList );
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

    @RequestMapping(value = "/saveNewChargeNetworkUser", method = RequestMethod.POST)
    public ModelAndView saveNewLocation( @Valid @ModelAttribute("chgNetworkUser") ChgUserBean chgUserBean, BindingResult bindingResult )
    {
        if( bindingResult.hasErrors() )
        {
            ModelAndView loadSignUpView = loadChgNetworkSignUpView();
            loadSignUpView.getModel().remove( "chgNetworkUser" );
            loadSignUpView.addObject( bindingResult );
            return loadSignUpView;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "user/chgNetwork/chgNetworkSignUpError" );
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
                profileNameBuilder.append( chgUserBean.getFirstName() );
                profileNameBuilder.append( " " );
                profileNameBuilder.append( chgUserBean.getLastName() );
                chgUserBean.setProfileName( profileNameBuilder.toString() );
                chgUserBean.setUserRole( UserRoles.CHG_NETWORK );
                AddUser newChgOwner = ChgUserDataLoader.createChargeUser( chgUserBean );
                remoteUserStoreManagerService.addUser( newChgOwner );
                modelAndView.setViewName( "user/chgNetwork/chgNetworkLoginSuccess" );
            }
            catch( SOAPFaultException e )
            {
                modelAndView.setViewName( "user/chgNetwork/chgNetworkSignUpError" );
                modelAndView.getModel().put( "error", e.getMessage() );
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
