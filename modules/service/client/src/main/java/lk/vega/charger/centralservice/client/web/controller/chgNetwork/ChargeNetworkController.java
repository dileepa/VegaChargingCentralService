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
import lk.vega.charger.util.StringUtil;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/15/15.
 * Time on 2:20 PM
 */
@Controller
public class ChargeNetworkController
{
    @RequestMapping(value = "/AllChargeNetworks", method = RequestMethod.GET)
    public ModelAndView viewAllNetworks()
    {
        ModelAndView modelAndView = new ModelAndView();
        ChgResponse allNetworkRes = ChargeNetworkLoader.loadAllChargeNetworks();
        if( allNetworkRes.isSuccess() )
        {
            List chargeNetworks = (List<ChargeNetwork>) allNetworkRes.getReturnData();
            List chargeNetworkBeanList = ChargeNetworkBean.getBeanList( chargeNetworks, DomainBeanImpl.CHARGE_NETWORK_BEAN_ID);
            modelAndView.getModel().put( "networks",chargeNetworkBeanList );
            modelAndView.setViewName( "chargeNetwork/chargeNetworks" );
        }

        return modelAndView;
    }


    @RequestMapping(value = "/chargeNetwork/addChargeNetwork", method = RequestMethod.GET)
    public ModelAndView addChargeNetwork()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "chargeNetwork/addChargeNetwork" );
        ChargeNetworkBean chargeNetworkBean = new ChargeNetworkBean();
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
            ChgResponse loadSavedNetworkStationsRes = ChargeNetworkLoader.loadNetworkSpecificStationIds( savedChargeNetwork.getNetworkId() );
            if( loadSavedNetworkStationsRes.isSuccess() )
            {
                List<ChargeNetworkAndStationMapping> savedChargeNetworkAndStationMappingList = (List) loadSavedNetworkStationsRes.getReturnData();
                List<String> chargeStationIds = new ArrayList<String>();
                for( ChargeNetworkAndStationMapping chargeNetworkAndStationMapping : savedChargeNetworkAndStationMappingList )
                {
                    chargeStationIds.add( String.valueOf( chargeNetworkAndStationMapping.getStationId() ) );
                }
                String commaSeparatedIds = StringUtil.getCommaSeparatedStringFromStringList( chargeStationIds );
                ChgResponse chargeStationDaResponse = ChargeStationLoader.loadSpecificStationsByIds( commaSeparatedIds );
                if( chargeStationDaResponse.isSuccess() )
                {
                    List<ChargePoint> loadChargePoints = (List) chargeStationDaResponse.getReturnData();
                    List allChargePointBeanList = ChargeStationBean.getBeanList( loadChargePoints, DomainBeanImpl.CHARGE_STATION_BEAN_ID );
                    modelAndView.getModel().put( "networkChargePoints", allChargePointBeanList );
                    modelAndView.getModel().put("loadMsg","Charger Network Load Successfully.");
                    modelAndView.setViewName( "chargeNetwork/loadChargeNetwork" );
                }
            }
        }
        return modelAndView;
    }

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
                ChargeNetworkBean chargeNetworkBeanSaved = new ChargeNetworkBean();
                chargeNetworkBeanSaved.createBean( savedChargeNetwork );
                modelAndView.getModel().put( "network", chargeNetworkBeanSaved );
                List chargeNetworkAndStationMappingList = new ArrayList<ChargeNetworkAndStationMapping>();
                for( String chargeStationID : chargeNetworkBean.getChargeStationIds() )
                {
                    ChargeNetworkAndStationMapping chargeNetworkAndStationMapping = new ChargeNetworkAndStationMapping();
                    chargeNetworkAndStationMapping.init();
                    chargeNetworkAndStationMapping.setNetworkId( savedChargeNetwork.getNetworkId() );
                    chargeNetworkAndStationMapping.setStationId( Integer.parseInt( chargeStationID ) );
                    chargeNetworkAndStationMapping.setStatus( Savable.NEW );
                    chargeNetworkAndStationMappingList.add( chargeNetworkAndStationMapping );
                }
                ChgResponse saveChargeNetworkStationRes = CoreController.save( chargeNetworkAndStationMappingList );
                if( saveChargeNetworkStationRes.isSuccess() )
                {
                    ChgResponse loadSavedNetworkStationsRes = ChargeNetworkLoader.loadNetworkSpecificStationIds( savedChargeNetwork.getNetworkId() );
                    if( loadSavedNetworkStationsRes.isSuccess() )
                    {
                        List<ChargeNetworkAndStationMapping> savedChargeNetworkAndStationMappingList = (List) loadSavedNetworkStationsRes.getReturnData();
                        List<String> chargeStationIds = new ArrayList<String>();
                        for( ChargeNetworkAndStationMapping chargeNetworkAndStationMapping : savedChargeNetworkAndStationMappingList )
                        {
                            chargeStationIds.add( String.valueOf( chargeNetworkAndStationMapping.getStationId() ) );
                        }
                        String commaSeparatedIds = StringUtil.getCommaSeparatedStringFromStringList( chargeStationIds );
                        ChgResponse chargeStationDaResponse = ChargeStationLoader.loadSpecificStationsByIds( commaSeparatedIds );
                        if( chargeStationDaResponse.isSuccess() )
                        {
                            List<ChargePoint> loadChargePoints = (List) chargeStationDaResponse.getReturnData();
                            List allChargePointBeanList = ChargeStationBean.getBeanList( loadChargePoints, DomainBeanImpl.CHARGE_STATION_BEAN_ID );
                            modelAndView.getModel().put( "networkChargePoints", allChargePointBeanList );
                            modelAndView.getModel().put("loadMsg","Charger Network is saved Successfully.");
                            modelAndView.setViewName( "chargeNetwork/loadChargeNetwork" );
                        }
                    }
                }
            }
        }
        return modelAndView;
    }
}
