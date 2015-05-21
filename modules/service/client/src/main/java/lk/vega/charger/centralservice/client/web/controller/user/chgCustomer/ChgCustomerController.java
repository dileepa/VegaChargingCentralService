package lk.vega.charger.centralservice.client.web.controller.user.chgCustomer;

import lk.vega.charger.centralservice.client.web.dataLoader.user.ChgUserDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.GenderDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.TitelDataLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.user.ChgUserBean;
import lk.vega.charger.centralservice.client.web.domain.user.GenderBean;
import lk.vega.charger.centralservice.client.web.domain.user.TitleBean;
import lk.vega.charger.centralservice.client.web.permission.UserRoles;
import lk.vega.charger.core.ChgCustomerUser;
import lk.vega.charger.core.ChgUser;
import lk.vega.charger.core.NFCReference;
import lk.vega.charger.userManagement.UserHandler;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.Savable;
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
 * Date on 4/30/15.
 * Time on 5:49 PM
 */
@Controller
public class ChgCustomerController
{

    @RequestMapping(value = "/ChgCustomerSignUp", method = RequestMethod.GET)
    public ModelAndView loadChgCustomerSignUpView()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "user/chgCustomer/chgCustomerSignUp" );
        ChgUserBean chgUserBean = new ChgUserBean();
        modelAndView.getModel().put( "chgCustomerUser", chgUserBean );
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

    @RequestMapping(value = "/saveNewChargeCustomer", method = RequestMethod.POST)
    public ModelAndView saveNewLocation( @Valid @ModelAttribute("chgCustomerUser") ChgUserBean chgUserBean, BindingResult bindingResult )
    {
        if( bindingResult.hasErrors() )
        {
            ModelAndView loadSignUpView = loadChgCustomerSignUpView();
            loadSignUpView.getModel().remove( "chgCustomerUser" );
            loadSignUpView.addObject( bindingResult );
            return loadSignUpView;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "user/chgCustomer/chgCustomerSignUpError" );
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
                chgUserBean.setUserRole( UserRoles.CHG_CUSTOMER );
                AddUser newChgCustomer = ChgUserDataLoader.createChargeUser( chgUserBean );
                remoteUserStoreManagerService.addUser( newChgCustomer );
                ChgCustomerUser chgCustomerUser = new ChgCustomerUser();
                chgCustomerUser.init();
                chgCustomerUser.setUserName( newChgCustomer.getUserName() );
                chgCustomerUser.setStatus( Savable.NEW );
                chgCustomerUser.setNfcRef( NFCReference.DUMMY_NFC_REF );
                chgCustomerUser.setUserType( ChgUser.USER_WEB_CUSTOMER );
                chgCustomerUser.setUserStatus( ChgUser.INACTIVE_USER );
                ChgResponse customerSaveRes = CoreController.save( chgCustomerUser );
                if (customerSaveRes.isSuccess())
                {
                    modelAndView.setViewName( "user/chgCustomer/chgCustomerSignUpSuccess" );
                }
                else
                {
                    modelAndView.setViewName( "user/chgCustomer/chgCustomerSignUpError" );
                    modelAndView.getModel().put( "error",customerSaveRes.getMsg() );
                }
            }
            catch( SOAPFaultException e )
            {
                modelAndView.setViewName( "user/chgCustomer/chgCustomerSignUpError" );
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
