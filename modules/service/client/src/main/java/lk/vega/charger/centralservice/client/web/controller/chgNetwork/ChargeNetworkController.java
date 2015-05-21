package lk.vega.charger.centralservice.client.web.controller.chgNetwork;

import lk.vega.charger.centralservice.client.web.dataLoader.chargeNetwork.ChargeNetworkLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.ChgUserDataLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.chargeNetwork.ChargeNetworkBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;
import lk.vega.charger.centralservice.client.web.domain.user.ChgUserBean;
import lk.vega.charger.centralservice.client.web.permission.UserRoles;
import lk.vega.charger.core.ChargeNetwork;
import lk.vega.charger.core.ChargeNetworkAndStationMapping;
import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.Savable;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/15/15.
 * Time on 2:20 PM
 */
@Controller
public class ChargeNetworkController
{
    //DONE
    @RequestMapping(value = "/AllChargeNetworks", method = RequestMethod.GET)
    public ModelAndView viewAllNetworks()
    {
        ModelAndView modelAndView = new ModelAndView();
        ChgResponse allNetworkRes = ChargeNetworkLoader.loadAllChargeNetworks();
        if( allNetworkRes.isSuccess() )
        {
            List chargeNetworks = (List<ChargeNetwork>) allNetworkRes.getReturnData();
            List chargeNetworkBeanList = ChargeNetworkBean.getBeanList( chargeNetworks, DomainBeanImpl.CHARGE_NETWORK_BEAN_ID );
            modelAndView.getModel().put( "networks", chargeNetworkBeanList );
            modelAndView.setViewName( "chargeNetwork/chargeNetworks" );
        }

        return modelAndView;
    }


    //DONE
    @RequestMapping(value = "/chargeNetwork/addChargeNetwork", method = RequestMethod.GET)
    public ModelAndView addChargeNetwork()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "chargeNetwork/addChargeNetwork" );
        ChargeNetwork chargeNetwork = new ChargeNetwork();
        chargeNetwork.init();
        ChargeNetworkBean chargeNetworkBean = new ChargeNetworkBean();
        chargeNetworkBean.createBean( chargeNetwork );
        modelAndView.getModel().put( "chargeNetwork", chargeNetworkBean );
        ChgResponse chgPointOwnersRes = ChgUserDataLoader.getUserListForSpecificRole( UserRoles.CHG_NETWORK );
        if( chgPointOwnersRes.isSuccess() )
        {
            List<ChgUserBean> chargeNetworkOwnersBeans = (List) chgPointOwnersRes.getReturnData();
            modelAndView.getModel().put( "networkOwners", chargeNetworkOwnersBeans );
        }

        ChgResponse chgResponse = ChargeStationLoader.loadAllChargePoints();
        if( chgResponse.isSuccess() )
        {
            List<ChargePoint> allChargePointList = (List) chgResponse.getReturnData();
            List allChargePointBeanList = ChargeStationBean.getBeanList( allChargePointList, DomainBeanImpl.CHARGE_STATION_BEAN_ID );
            modelAndView.getModel().put( "chgStations", allChargePointBeanList );
        }

        return modelAndView;
    }

    //DONE
    @RequestMapping(value = "/chargeNetwork/loadChargeNetwork", method = RequestMethod.GET)
    public ModelAndView loadChargeNetwork( SecurityContextHolderAwareRequestWrapper request, @RequestParam(value = "networkID", required = false) int networkID )
    {
        ChgResponse loadSavedNetworkRes = ChargeNetworkLoader.loadSpecificChargeNetworkById( networkID );
        ModelAndView modelAndView = new ModelAndView();
        if( loadSavedNetworkRes.isSuccess() )
        {
            ChargeNetwork savedChargeNetwork = (ChargeNetwork) loadSavedNetworkRes.getReturnData();
            ChargeNetworkBean chargeNetworkBeanSaved = new ChargeNetworkBean();
            chargeNetworkBeanSaved.createBean( savedChargeNetwork );
            modelAndView.getModel().put( "network", chargeNetworkBeanSaved );
            modelAndView.getModel().put( "loadMsg", "Charger Network Load Successfully." );
            modelAndView.setViewName( "chargeNetwork/loadChargeNetwork" );
        }
        return modelAndView;
    }


    @RequestMapping(value = "chargeNetwork/editChargeNetwork", method = RequestMethod.GET)
    public ModelAndView editChargeNetwork( SecurityContextHolderAwareRequestWrapper request, @RequestParam(value = "networkID", required = false) int networkID )
    {
        ChgResponse loadSavedNetworkRes = ChargeNetworkLoader.loadSpecificChargeNetworkById( networkID );
        ModelAndView modelAndView = new ModelAndView();
        if( loadSavedNetworkRes.isSuccess() )
        {
            ChargeNetwork savedChargeNetwork = (ChargeNetwork) loadSavedNetworkRes.getReturnData();
            ChargeNetworkBean chargeNetworkBeanSaved = new ChargeNetworkBean();
            chargeNetworkBeanSaved.createBean( savedChargeNetwork );
            modelAndView.getModel().put( "chargeNetwork", chargeNetworkBeanSaved );
            ChgResponse chgPointOwnersRes = ChgUserDataLoader.getUserListForSpecificRole( UserRoles.CHG_NETWORK );
            if( chgPointOwnersRes.isSuccess() )
            {
                List<ChgUserBean> chargeNetworkOwnersBeans = (List) chgPointOwnersRes.getReturnData();
                for( ChgUserBean chgUserBean : chargeNetworkOwnersBeans )
                {
                    chgUserBean.setSelected( chgUserBean.getUserName().equals( chargeNetworkBeanSaved.getNetworkOwnerUserName() ) );
                }
                modelAndView.getModel().put( "networkOwners", chargeNetworkOwnersBeans );
            }
            ChgResponse loadAllChargeStationDataResponse = ChargeStationLoader.loadAllChargePoints();
            if( loadAllChargeStationDataResponse.isSuccess() )
            {
                List<ChargePoint> loadChargePoints = (List) loadAllChargeStationDataResponse.getReturnData();
                List allChargePointBeanList = ChargeStationBean.getBeanList( loadChargePoints, DomainBeanImpl.CHARGE_STATION_BEAN_ID );
                for( ChargeStationBean chargeStationBean : (List<ChargeStationBean>) allChargePointBeanList )
                {
                    if( chargeNetworkBeanSaved.getChargeStationIds().contains( String.valueOf( chargeStationBean.getChargePointId() ) ) )
                    {
                        chargeStationBean.setSelected( true );
                    }
                }
                modelAndView.getModel().put( "chgStations", allChargePointBeanList );
            }
        }
        return modelAndView;
    }

    //DONE
    @RequestMapping(value = "/saveNewChargeNetwork", method = RequestMethod.POST)
    public ModelAndView saveNewLocation( SecurityContextHolderAwareRequestWrapper request, @ModelAttribute("chargeNetwork") ChargeNetworkBean chargeNetworkBean )
    {
        ModelAndView modelAndView = new ModelAndView();

        ChargeNetwork chargeNetwork = new ChargeNetwork();
        chargeNetworkBean.decodeBeanToReal( chargeNetwork );
        chargeNetwork.setStatus( Savable.NEW );
        ChgResponse saveNetworkRes = CoreController.save( chargeNetwork );
        if( saveNetworkRes.isSuccess() )
        {
            ChgResponse loadSavedNetworkRes = ChargeNetworkLoader.loadSpecificChargeNetworkByReference( chargeNetwork.getReference() );
            if( loadSavedNetworkRes.isSuccess() )
            {
                ChargeNetwork savedChargeNetwork = (ChargeNetwork) loadSavedNetworkRes.getReturnData();
                chargeNetworkBean.setNetworkId( savedChargeNetwork.getNetworkId() );
                List<ChargeNetworkAndStationMapping> chargeNetworkAndStationMappingList = chargeNetworkBean.createNetworkAndStationMappingList();
                ChgResponse saveChargeNetworkStationRes = CoreController.save( chargeNetworkAndStationMappingList );
                if( saveChargeNetworkStationRes.isSuccess() )
                {
                    ChargeNetworkBean chargeNetworkBeanSaved = new ChargeNetworkBean();
                    chargeNetworkBeanSaved.createBean( savedChargeNetwork );
                    modelAndView.getModel().put( "network", chargeNetworkBeanSaved );
                    modelAndView.getModel().put( "loadMsg", "Charger Network is saved Successfully." );
                    modelAndView.setViewName( "chargeNetwork/loadChargeNetwork" );
                }
            }
        }
        return modelAndView;
    }


    @RequestMapping(value = "/saveExistingChargeNetwork", method = RequestMethod.POST)
    public ModelAndView saveExistingLocation( SecurityContextHolderAwareRequestWrapper request, @ModelAttribute("chargeNetwork") ChargeNetworkBean chargeNetworkBean )
    {
        ModelAndView modelAndView = new ModelAndView();
        ChargeNetwork chargeNetwork = new ChargeNetwork();
        chargeNetworkBean.decodeBeanToReal( chargeNetwork );
        chargeNetwork.setStatus( Savable.MODIFIED );
        ChgResponse saveNetworkRes = CoreController.save( chargeNetwork );
        if( saveNetworkRes.isSuccess() )
        {
            ChgResponse loadSavedNetworkRes = ChargeNetworkLoader.loadSpecificChargeNetworkByReference( chargeNetwork.getReference() );
            if( loadSavedNetworkRes.isSuccess() )
            {
                ChargeNetwork savedChargeNetwork = (ChargeNetwork) loadSavedNetworkRes.getReturnData();
                ChargeNetworkBean chargeNetworkBeanSaved = new ChargeNetworkBean();
                ChgResponse loadExistingChgPointIDSRes = ChargeNetworkLoader.loadNetworkSpecificStationIds( savedChargeNetwork.getNetworkId() );
                if( loadExistingChgPointIDSRes.isSuccess() )
                {
                    List<ChargeNetworkAndStationMapping> existingChargeIdsList = (List<ChargeNetworkAndStationMapping>) loadExistingChgPointIDSRes.getReturnData();
                    Map<Integer, ChargeNetworkAndStationMapping> existingChargeStationIDMap = new HashMap<Integer, ChargeNetworkAndStationMapping>();
                    for( ChargeNetworkAndStationMapping chargeNetworkAndStationMapping : existingChargeIdsList )
                    {
                        chargeNetworkAndStationMapping.setStatus( Savable.DELETED );
                        existingChargeStationIDMap.put( chargeNetworkAndStationMapping.getStationId(), chargeNetworkAndStationMapping );
                    }

                    //FILTERING WHICH STATION ARE NEWLY ASSIGNED, AND WHAT WE DO DELETE

                    for( String chargeID : chargeNetworkBean.getChargeStationIds() )
                    {
                        if( existingChargeStationIDMap.containsKey( Integer.parseInt( chargeID ) ) )
                        {
                            ChargeNetworkAndStationMapping existingChargeNetworkAndStationMapping = existingChargeStationIDMap.get( Integer.parseInt( chargeID ) );
                            existingChargeNetworkAndStationMapping.setStatus( Savable.UNCHANGED );
                        }
                        else
                        {
                            ChargeNetworkAndStationMapping newCreatedChargeNetworkAndStationMapping = new ChargeNetworkAndStationMapping();
                            newCreatedChargeNetworkAndStationMapping.init();
                            newCreatedChargeNetworkAndStationMapping.setNetworkId( savedChargeNetwork.getNetworkId() );
                            newCreatedChargeNetworkAndStationMapping.setStationId( Integer.parseInt( chargeID ) );
                            newCreatedChargeNetworkAndStationMapping.setStatus( Savable.NEW );
                            existingChargeStationIDMap.put( newCreatedChargeNetworkAndStationMapping.getStationId(), newCreatedChargeNetworkAndStationMapping );
                        }
                    }
                    ChgResponse saveChargeNetworkAndStationMappingRes = CoreController.save( new ArrayList<ChargeNetworkAndStationMapping>( existingChargeStationIDMap.values() ) );
                    if( saveChargeNetworkAndStationMappingRes.isSuccess() )
                    {
                        chargeNetworkBeanSaved.createBean( savedChargeNetwork );
                        modelAndView.getModel().put( "network", chargeNetworkBeanSaved );
                        modelAndView.getModel().put( "loadMsg", "Charger Network is saved Successfully." );
                        modelAndView.setViewName( "chargeNetwork/loadChargeNetwork" );
                    }
                }
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/chargeNetwork/deleteConfirmationChargeNetwork", method = RequestMethod.GET)
    public ModelAndView deleteConfirmNetwork( @RequestParam(value = "networkID", required = false) Integer networkID )
    {
        ModelAndView modelAndView = new ModelAndView();
        ChgResponse loadedChargeNetworkResponse = ChargeNetworkLoader.loadSpecificChargeNetworkById( networkID );
        if( loadedChargeNetworkResponse.isSuccess() )
        {
            ChargeNetwork chargeNetwork = (ChargeNetwork) loadedChargeNetworkResponse.getReturnData();
            ChargeNetworkBean updateChargeNetworkBean = new ChargeNetworkBean();
            updateChargeNetworkBean.createBean( chargeNetwork );
            modelAndView.setViewName( "chargeNetwork/deleteConfirmationChargeNetwork" );
            modelAndView.getModel().put( "network", updateChargeNetworkBean );
        }
        return modelAndView;
    }

    @RequestMapping(value = "/deleteChargeNetwork", method = RequestMethod.POST)
    public ModelAndView deleteChargeStation( @ModelAttribute("network") ChargeNetworkBean chargeNetworkBean )
    {
        ChargeNetwork chargeNetwork = new ChargeNetwork();
        chargeNetwork.init();
        chargeNetworkBean.decodeBeanToReal( chargeNetwork );
        chargeNetwork.setStatus( Savable.DELETED );
        ChgResponse chgResponse = CoreController.save( chargeNetwork );
        ModelAndView modelAndView = null;
        if( chgResponse.isSuccess() )
        {
            modelAndView = viewAllNetworks();
        }
        else
        {
            modelAndView = new ModelAndView();
            modelAndView.setViewName( "chargeStation/errorChargeStation" );
            modelAndView.getModel().put( "chargeStationErrorMsg", "Error in Deleting Charging Point - " + chargeNetworkBean.getReference() );
        }
        return modelAndView;

    }
}
