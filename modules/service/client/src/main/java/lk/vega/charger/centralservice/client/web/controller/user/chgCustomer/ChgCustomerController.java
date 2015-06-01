package lk.vega.charger.centralservice.client.web.controller.user.chgCustomer;

import lk.vega.charger.centralservice.client.web.dataLoader.chargeNetwork.ChargeNetworkLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.level2Charger.LevelTwoChargerDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.ChgCustomerDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.ChgUserDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.GenderDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.NFCReferenceDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.TitelDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.UserStatusDataLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.chargeNetwork.ChargeNetworkBean;
import lk.vega.charger.centralservice.client.web.domain.user.ChgUserBean;
import lk.vega.charger.centralservice.client.web.domain.user.GenderBean;
import lk.vega.charger.centralservice.client.web.domain.user.TitleBean;
import lk.vega.charger.centralservice.client.web.domain.user.User;
import lk.vega.charger.centralservice.client.web.domain.user.UserStatusBean;
import lk.vega.charger.centralservice.client.web.domain.user.chgCustomer.ChgCustomerBean;
import lk.vega.charger.centralservice.client.web.permission.UserRoles;
import lk.vega.charger.core.ChargeNetwork;
import lk.vega.charger.core.ChgCustomerUser;
import lk.vega.charger.core.ChgLevelTwoTransaction;
import lk.vega.charger.core.ChgUser;
import lk.vega.charger.core.NFCReference;
import lk.vega.charger.userManagement.UserHandler;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.Savable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.wso2.carbon.um.ws.service.AddUser;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServicePortType;

import javax.validation.Valid;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/30/15.
 * Time on 5:49 PM
 */
@Controller
public class ChgCustomerController
{

    @RequestMapping(value = "/ViewAllCustomers", method = RequestMethod.GET)
    public ModelAndView loadAllCustomersView()
    {
        ModelAndView modelAndView = new ModelAndView();
        ChgResponse allInactiveCustomersRes = ChgCustomerDataLoader.loadAllCustomerList();
        if( allInactiveCustomersRes.isSuccess() )
        {
            List allInactiveCustomers = (List<ChgCustomerUser>) allInactiveCustomersRes.getReturnData();
            List allInactiveCustomersBeanList = ChgCustomerBean.getBeanList( allInactiveCustomers, DomainBeanImpl.USER_CUSTOMER_BEAN_ID );
            modelAndView.getModel().put( "Customers", allInactiveCustomersBeanList );
            modelAndView.getModel().put( "cusMsg", "All Customers" );
            modelAndView.setViewName( "user/chgCustomer/customers" );
        }

        return modelAndView;

    }

    @RequestMapping(value = "/ViewAllActiveCustomers", method = RequestMethod.GET)
    public ModelAndView loadAllActiveCustomersView()
    {
        ModelAndView modelAndView = new ModelAndView();
        ChgResponse allInactiveCustomersRes = ChgCustomerDataLoader.loadCustomerListBySpecificSUserStatus( ChgUser.ACTIVE_USER );
        if( allInactiveCustomersRes.isSuccess() )
        {
            List allInactiveCustomers = (List<ChgCustomerUser>) allInactiveCustomersRes.getReturnData();
            List allInactiveCustomersBeanList = ChgCustomerBean.getBeanList( allInactiveCustomers, DomainBeanImpl.USER_CUSTOMER_BEAN_ID );
            modelAndView.getModel().put( "Customers", allInactiveCustomersBeanList );
            modelAndView.getModel().put( "cusMsg", "All Active Customers" );
            modelAndView.setViewName( "user/chgCustomer/customers" );
        }

        return modelAndView;

    }

    @RequestMapping(value = "/ViewAllInActiveCustomers", method = RequestMethod.GET)
    public ModelAndView loadAllInActiveCustomersView()
    {
        ModelAndView modelAndView = new ModelAndView();
        ChgResponse allInactiveCustomersRes = ChgCustomerDataLoader.loadCustomerListBySpecificSUserStatus( ChgUser.INACTIVE_USER );
        if( allInactiveCustomersRes.isSuccess() )
        {
            List allInactiveCustomers = (List<ChgCustomerUser>) allInactiveCustomersRes.getReturnData();
            List allInactiveCustomersBeanList = ChgCustomerBean.getBeanList( allInactiveCustomers, DomainBeanImpl.USER_CUSTOMER_BEAN_ID );
            modelAndView.getModel().put( "Customers", allInactiveCustomersBeanList );
            modelAndView.getModel().put( "cusMsg", "All In Active Customers" );
            modelAndView.setViewName( "user/chgCustomer/customers" );
        }

        return modelAndView;

    }

    @RequestMapping(value = "/AllCustomerSpecificTransaction", method = RequestMethod.GET)
    public ModelAndView viewAllCustomerSpecificTransaction(SecurityContextHolderAwareRequestWrapper request)
    {
        User loggedUser = ( User)( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getPrincipal();
        ModelAndView modelAndView = new ModelAndView();
        ChgResponse transactionsRes = LevelTwoChargerDataLoader.loadCustomerSpecificTransactions( loggedUser.getUsername() );
        if( transactionsRes.isSuccess() )
        {
            List transactions = (List<ChgLevelTwoTransaction>) transactionsRes.getReturnData();
            modelAndView.getModel().put( "transactions", transactions );
            modelAndView.setViewName( "user/chgCustomer/allTransactions" );
        }

        return modelAndView;
    }

    @RequestMapping(value = "/saveExistingChgCustomer", method = RequestMethod.POST)
    public ModelAndView saveExistingChgCustomer( SecurityContextHolderAwareRequestWrapper request, @Valid @ModelAttribute("chgCustomer") ChgCustomerBean chgCustomerBean, BindingResult bindingResult )
    {
        if( bindingResult.getFieldError( "nfcRef" ) != null )
        {
            ModelAndView editChargeNetworkView = editChargeNetwork( request, chgCustomerBean.getUserId() );
            editChargeNetworkView.getModel().remove( "errorMsg" );
            editChargeNetworkView.getModel().put( "errorMsg", bindingResult.getFieldError( "nfcRef" ).getDefaultMessage() );
            return editChargeNetworkView;
        }
        ModelAndView modelAndView = new ModelAndView();
        ChgCustomerUser chgCustomerUser = new ChgCustomerUser();
        chgCustomerBean.decodeBeanToReal( chgCustomerUser );

        ChgResponse loadExistingNetworkIDSRes = NFCReferenceDataLoader.loadNetworksBySpecificNFC( chgCustomerBean.getNfcRef() );
        if( loadExistingNetworkIDSRes.isSuccess() )
        {
            List<NFCReference> existingNetworkIdsList = (List<NFCReference>) loadExistingNetworkIDSRes.getReturnData();
            if( existingNetworkIdsList.isEmpty() )
            {
                List<String> newlyAddedNetworksIds = chgCustomerBean.getNetworkIds();
                for( String networkID : newlyAddedNetworksIds )
                {
                    NFCReference nfcReference = new NFCReference();
                    nfcReference.init();
                    nfcReference.setUserName( chgCustomerBean.getUserName() );
                    nfcReference.setReference( chgCustomerBean.getNfcRef() );
                    nfcReference.setNetworkId( Integer.parseInt( networkID ) );
                    nfcReference.setStatus( Savable.NEW );
                    chgCustomerUser.getNfcReferenceList().add( nfcReference );
                }
            }
            else
            {
                Map<Integer, NFCReference> existingNetworkIDMap = new HashMap<Integer, NFCReference>();
                for( NFCReference nfcReference : existingNetworkIdsList )
                {
                    nfcReference.setStatus( Savable.DELETED );
                    existingNetworkIDMap.put( nfcReference.getNetworkId(), nfcReference );
                }

                //FILTERING WHICH NETWORKS ARE NEWLY ASSIGNED, AND WHAT WE DO DELETE

                for( String netWorkID : chgCustomerBean.getNetworkIds() )
                {
                    if( existingNetworkIDMap.containsKey( Integer.parseInt( netWorkID ) ) )
                    {
                        NFCReference existingNFCReference = existingNetworkIDMap.get( Integer.parseInt( netWorkID ) );
                        existingNFCReference.setStatus( Savable.UNCHANGED );
                    }
                    else
                    {
                        NFCReference newCreatedNFCReference = new NFCReference();
                        newCreatedNFCReference.init();
                        newCreatedNFCReference.setUserName( chgCustomerBean.getUserName() );
                        newCreatedNFCReference.setReference( chgCustomerBean.getNfcRef() );
                        newCreatedNFCReference.setNetworkId( Integer.parseInt( netWorkID ) );
                        newCreatedNFCReference.setStatus( Savable.NEW );
                        existingNetworkIDMap.put( newCreatedNFCReference.getNetworkId(), newCreatedNFCReference );
                    }
                }

                chgCustomerUser.setNfcReferenceList( new ArrayList<NFCReference>( existingNetworkIDMap.values() ) );
            }
            chgCustomerUser.setStatus( Savable.MODIFIED );
            ChgResponse saveChgCustomerRes = CoreController.save( chgCustomerUser );
            if( saveChgCustomerRes.isSuccess() )
            {
                ChgResponse loadChgCustomerRes = ChgCustomerDataLoader.loadCustomerByUserID( chgCustomerUser.getUserId() );
                if( loadChgCustomerRes.isSuccess() )
                {
                    ChgCustomerUser customerUser = (ChgCustomerUser) loadChgCustomerRes.getReturnData();
                    ChgCustomerBean chargeCustomerBeanSaved = new ChgCustomerBean();
                    chargeCustomerBeanSaved.createBean( customerUser );
                    modelAndView.getModel().put( "chgCustomer", chargeCustomerBeanSaved );
                    modelAndView.getModel().put( "loadMsg", "Customer is saved Successfully." );
                    modelAndView.setViewName( "user/chgCustomer/loadChgCustomer" );

                }
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/user/chgCustomer/loadChgCustomer", method = RequestMethod.GET)
    public ModelAndView loadChargeCustomer( SecurityContextHolderAwareRequestWrapper request, @RequestParam(value = "userID", required = false) int userId )
    {
        ModelAndView modelAndView = new ModelAndView(  );
        ChgResponse loadChgCustomerRes = ChgCustomerDataLoader.loadCustomerByUserID( userId );
        if( loadChgCustomerRes.isSuccess() )
        {
            ChgCustomerUser customerUser = (ChgCustomerUser) loadChgCustomerRes.getReturnData();
            ChgCustomerBean chargeCustomerBeanSaved = new ChgCustomerBean();
            chargeCustomerBeanSaved.createBean( customerUser );
            modelAndView.getModel().put( "chgCustomer", chargeCustomerBeanSaved );
            modelAndView.getModel().put( "loadMsg", "Customer is loaded Successfully." );
            modelAndView.setViewName( "user/chgCustomer/loadChgCustomer" );

        }
        return modelAndView;
    }

    @RequestMapping(value = "/user/chgCustomer/editChgCustomer", method = RequestMethod.GET)
    public ModelAndView editChargeNetwork( SecurityContextHolderAwareRequestWrapper request, @RequestParam(value = "userID", required = false) int userId )
    {
        ChgResponse loadSavedChgCustomerRes = ChgCustomerDataLoader.loadCustomerByUserID( userId );
        ModelAndView modelAndView = new ModelAndView();
        if( loadSavedChgCustomerRes.isSuccess() )
        {
            ChgCustomerUser chgCustomerUser = (ChgCustomerUser) loadSavedChgCustomerRes.getReturnData();
            ChgCustomerBean chgCustomerBean = new ChgCustomerBean();
            chgCustomerBean.createBean( chgCustomerUser );
            modelAndView.getModel().put( "chgCustomer", chgCustomerBean );
            modelAndView.getModel().put( "errorMsg", "" );
            ChgResponse userStatusRes = UserStatusDataLoader.loadAllUserStatusProperties();
            if( userStatusRes.isSuccess() )
            {
                List<String> userStatusList = (List) userStatusRes.getReturnData();
                List userStatusBeanList = UserStatusBean.getBeanList( userStatusList, DomainBeanImpl.USER_STATUS_BEAN_ID );
                for( UserStatusBean userStatusBean : (List<UserStatusBean>) userStatusBeanList )
                {
                    userStatusBean.setSelected( chgCustomerBean.getUserStatus().equals( userStatusBean.getName() ) );
                }
                modelAndView.getModel().put( "userStatusList", userStatusBeanList );
            }
            ChgResponse loadAllNetworksRes = ChargeNetworkLoader.loadAllChargeNetworks();
            if( loadAllNetworksRes.isSuccess() )
            {
                List<ChargeNetwork> allNetworks = (List<ChargeNetwork>) loadAllNetworksRes.getReturnData();
                List allNetworkBeans = ChargeNetworkBean.getBeanList( allNetworks, DomainBeanImpl.CHARGE_NETWORK_BEAN_ID );
                for (ChargeNetworkBean chargeNetworkBean : (List<ChargeNetworkBean>)allNetworkBeans )
                {
                    chargeNetworkBean.setSelected( chgCustomerBean.getNetworkIds().contains( String.valueOf( chargeNetworkBean.getNetworkId() ) ) );
                }
                modelAndView.getModel().put( "networkList", allNetworkBeans );
            }
            modelAndView.setViewName( "user/chgCustomer/editChgCustomer" );
        }
        return modelAndView;
    }


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
                String dummyNfcRef = createDummyRef ( chgCustomerUser.getUserName());
                chgCustomerUser.setNfcRef( dummyNfcRef );
                chgCustomerUser.setUserType( ChgUser.USER_CUSTOMER );
                chgCustomerUser.setCreatedBy( ChgUser.CREATED_BY_ONLINE );
                chgCustomerUser.setUserStatus( ChgUser.INACTIVE_USER );
                ChgResponse customerSaveRes = CoreController.save( chgCustomerUser );
                if( customerSaveRes.isSuccess() )
                {
                    modelAndView.setViewName( "user/chgCustomer/chgCustomerSignUpSuccess" );
                }
                else
                {
                    modelAndView.setViewName( "user/chgCustomer/chgCustomerSignUpError" );
                    modelAndView.getModel().put( "error", customerSaveRes.getMsg() );
                }
            }
            catch( SOAPFaultException e )
            {
                modelAndView.setViewName( "user/chgCustomer/chgCustomerSignUpError" );
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

    private String createDummyRef( String userName )
    {
        StringBuilder sb = new StringBuilder(  );
        sb.append( NFCReference.DUMMY_NFC_REF );
        sb.append( "_" );
        sb.append( userName );
        return sb.toString();
    }

}
