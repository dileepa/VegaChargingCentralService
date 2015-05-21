package lk.vega.charger.centralservice.client.web.controller.chargeStation;

import lk.vega.charger.centralservice.client.web.dataLoader.chargeNetwork.ChargeNetworkLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationAvailabilityStatusLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationPowerStatusLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationProtocolLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationTypeLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.loacation.LocationLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.ChgUserDataLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.chargeNetwork.ChargeNetworkBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationAvailabilityStatusBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationPowerStatusBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationProtocolBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationTypeBean;
import lk.vega.charger.centralservice.client.web.domain.location.LocationBean;
import lk.vega.charger.centralservice.client.web.domain.user.ChgUserBean;
import lk.vega.charger.centralservice.client.web.domain.user.User;
import lk.vega.charger.centralservice.client.web.permission.UserRoles;
import lk.vega.charger.core.ChargeLocation;
import lk.vega.charger.core.ChargeNetwork;
import lk.vega.charger.core.ChargeNetworkAndStationMapping;
import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.StringUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
 * Date on 4/24/15.
 * Time on 1:12 PM
 */
@Controller
public class ChargeStationsController
{

    @RequestMapping(value = "/AllChargeStations", method = RequestMethod.GET)
    public ModelAndView index()
    {
        ChgResponse chgResponse = ChargeStationLoader.loadAllChargePoints();
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            List<ChargePoint> allChargePointList = (List)chgResponse.getReturnData();
            List allChargePointBeanList = ChargeStationBean.getBeanList( allChargePointList, DomainBeanImpl.CHARGE_STATION_BEAN_ID );
            modelAndView.setViewName( "chargeStation/chargeStations" );
            modelAndView.getModel().put( "chgStations", allChargePointBeanList );
        }
        return modelAndView;
    }

    @RequestMapping(value = "/chargeStation/deleteConfirmationChargeStation", method = RequestMethod.GET)
    public ModelAndView deleteConfirm(@RequestParam(value = "chgStationID", required = false ) Integer chgStationID )
    {
        ModelAndView modelAndView = new ModelAndView(  );
        ChgResponse loadedChargePointResponse = ChargeStationLoader.loadSpecificChargePointByPointID( chgStationID );
        if (loadedChargePointResponse.isSuccess())
        {
            ChargePoint updatedChargePoint = (ChargePoint)loadedChargePointResponse.getReturnData();
            ChargeStationBean updateChargeStationBean = new ChargeStationBean();
            updateChargeStationBean.createBean( updatedChargePoint );
            modelAndView.setViewName( "chargeStation/deleteConfirmationChargeStation" );
            modelAndView.getModel().put( "chargeStation", updateChargeStationBean );
        }
        return modelAndView;
    }

    @RequestMapping(value = "/chargeStation/loadChargeStation", method = RequestMethod.GET)
    public ModelAndView viewStationDetails(@RequestParam(value = "chgStationID", required = false ) Integer chgStationID )
    {
        ModelAndView modelAndView = new ModelAndView(  );
        ChgResponse loadedChargePointResponse = ChargeStationLoader.loadSpecificChargePointByPointID( chgStationID );
        if (loadedChargePointResponse.isSuccess())
        {
            ChargePoint updatedChargePoint = (ChargePoint)loadedChargePointResponse.getReturnData();
            ChargeStationBean updateChargeStationBean = new ChargeStationBean();
            updateChargeStationBean.createBean( updatedChargePoint );
            modelAndView.setViewName( "chargeStation/deleteConfirmationChargeStation" );
            modelAndView.getModel().put( "chargeStation", updateChargeStationBean );

            List allChargeNetworkBeanListUnderStation = new ArrayList<ChargeNetworkBean>();
            ChgResponse assignedNetworkRes = ChargeStationLoader.loadChargeStationSpecificNetworkIds( updateChargeStationBean.getChargePointId() );
            if (assignedNetworkRes.isSuccess())
            {
                List<ChargeNetworkAndStationMapping> savedChargeNetworkAndStationMappingList = (List) assignedNetworkRes.getReturnData();
                List<String> networkIDs = new ArrayList<String>();
                for( ChargeNetworkAndStationMapping chargeNetworkAndStationMapping : savedChargeNetworkAndStationMappingList )
                {
                    networkIDs.add( String.valueOf( chargeNetworkAndStationMapping.getNetworkId() ) );
                }
                if( !networkIDs.isEmpty() )
                {
                    String commaSeparatedIds = StringUtil.getCommaSeparatedStringFromStringList( networkIDs );
                    ChgResponse chargeNetworkResponse = ChargeNetworkLoader.loadSpecificNetworksByIds( commaSeparatedIds );
                    if( chargeNetworkResponse.isSuccess() )
                    {
                        List<ChargeNetwork> loadChargeNetworks = (List) chargeNetworkResponse.getReturnData();
                        allChargeNetworkBeanListUnderStation = ChargeStationBean.getBeanList( loadChargeNetworks, DomainBeanImpl.CHARGE_NETWORK_BEAN_ID );
                    }
                }
            }
        }
        return modelAndView;
    }



    @RequestMapping(value = "/chargeStation/editChargeStation", method = RequestMethod.GET)
    public ModelAndView editLocation(@RequestParam(value = "chgStationID", required = false ) Integer chgStationID )
    {
        ChgResponse chgResponse = ChargeStationLoader.loadSpecificChargePointByPointID(chgStationID);
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChargePoint chargePoint = (ChargePoint)chgResponse.getReturnData();
            ChargeStationBean chargeStationBean = new ChargeStationBean();
            chargeStationBean.createBean( chargePoint );
            modelAndView.setViewName( "chargeStation/editChargeStation" );
            modelAndView.getModel().put( "chargeStation", chargeStationBean );
            ChgResponse chgLocationsResponse = LocationLoader.loadAllChargeLocations();
            if (chgLocationsResponse.isSuccess())
            {
                List<ChargeLocation> chargeLocations = (List)chgLocationsResponse.getReturnData();
                List locationBeanList = LocationBean.getBeanList( chargeLocations, DomainBeanImpl.LOCATION_BEAN_ID );
                for( LocationBean locationBean : (List<LocationBean>) locationBeanList )
                {
                    locationBean.setSelected( chargeStationBean.getLocationId() == locationBean.getLocationId() );
                }
                modelAndView.getModel().put( "locations", locationBeanList );
            }
            ChgResponse chgPointPowerStatusRes = ChargeStationPowerStatusLoader.loadAllChargePointPowerStatus();
            if( chgPointPowerStatusRes.isSuccess() )
            {
                List<String> chargePointPowerStatus = (List) chgPointPowerStatusRes.getReturnData();
                List powerStatusBeanList = ChargeStationPowerStatusBean.getBeanList( chargePointPowerStatus, DomainBeanImpl.CHARGE_STATION_POWER_STATUS_BEAN_ID );
                for( ChargeStationPowerStatusBean chargeStationPowerStatusBean : (List<ChargeStationPowerStatusBean>)powerStatusBeanList)
                {
                    chargeStationPowerStatusBean.setSelected( chargeStationPowerStatusBean.getName().equals( chargeStationBean.getChargePointPowerStatus() ) );
                }
                modelAndView.getModel().put( "powerStatusList",powerStatusBeanList );
            }
            ChgResponse chgPointProtocolsRes = ChargeStationProtocolLoader.loadAllChargePointProtocols();
            if( chgPointProtocolsRes.isSuccess() )
            {
                List<String> chargePointProtocols = (List) chgPointProtocolsRes.getReturnData();
                List protocolsBeanList = ChargeStationProtocolBean.getBeanList( chargePointProtocols, DomainBeanImpl.CHARGE_STATION_PROTOCOL_BEAN_ID );
                for( ChargeStationProtocolBean chargeStationProtocolBean : (List<ChargeStationProtocolBean>)protocolsBeanList)
                {
                    chargeStationProtocolBean.setSelected( chargeStationProtocolBean.getName().equals( chargeStationBean.getProtocol() ) );
                }
                modelAndView.getModel().put( "protocolList",protocolsBeanList );
            }
            ChgResponse chgPointTypesRes = ChargeStationTypeLoader.loadAllChargePointTypes();
            if( chgPointTypesRes.isSuccess() )
            {
                List<String> chargePointTypes = (List) chgPointTypesRes.getReturnData();
                List typesBeanList = ChargeStationTypeBean.getBeanList( chargePointTypes, DomainBeanImpl.CHARGE_STATION_TYPE_BEAN_ID );
                for( ChargeStationTypeBean chargeStationTypeBean : (List<ChargeStationTypeBean>)typesBeanList)
                {
                    chargeStationTypeBean.setSelected( chargeStationTypeBean.getName().equals( chargeStationBean.getType() ) );
                }
                modelAndView.getModel().put( "typeList",typesBeanList );
            }
            ChgResponse chgPointOwnersRes = ChgUserDataLoader.getUserListForSpecificRole( UserRoles.CHG_OWNER);
            if( chgPointOwnersRes.isSuccess() )
            {
                List<ChgUserBean> chargeOwnerBeans = (List) chgPointOwnersRes.getReturnData();
                for( ChgUserBean chgUserBean : chargeOwnerBeans)
                {
                    chgUserBean.setSelected( chgUserBean.getUserName().equals( chargeStationBean.getUserName() ) );
                }
                modelAndView.getModel().put( "owners",chargeOwnerBeans );
            }
            ChgResponse chgPointStatusAvailabilityRes = ChargeStationAvailabilityStatusLoader.loadAllChargePointStatus();
            if( chgPointStatusAvailabilityRes.isSuccess() )
            {
                List<String> chargePointAvailabilityStatus = (List) chgPointStatusAvailabilityRes.getReturnData();
                List availabilityStatusBeanList = ChargeStationAvailabilityStatusBean.getBeanList( chargePointAvailabilityStatus, DomainBeanImpl.CHARGE_STATION_AVAILABILITY_STATUS_BEAN_ID );
                for( ChargeStationAvailabilityStatusBean status : (List<ChargeStationAvailabilityStatusBean>) availabilityStatusBeanList )
                {
                    status.setSelected( status.getName().equals( chargeStationBean.getChargePointAvailabilityStatus() ) );
                }
                modelAndView.getModel().put( "availabilityStatusList",availabilityStatusBeanList );
            }
        } return modelAndView;
    }

    @RequestMapping(value = "/deleteChargeStation", method = RequestMethod.POST)
    public ModelAndView deleteChargeStation(@ModelAttribute("chargeStation" ) ChargeStationBean chargeStationBean )
    {
        ChargePoint chargePoint = new ChargePoint();
        chargePoint.init();
        chargeStationBean.decodeBeanToReal( chargePoint );
        chargePoint.setStatus( Savable.DELETED );
        ChgResponse chgResponse = CoreController.save( chargePoint );
        ModelAndView modelAndView = null;
        if (chgResponse.isSuccess())
        {
            modelAndView = index();
        }
        else
        {
            modelAndView = new ModelAndView(  );
            modelAndView.setViewName( "chargeStation/errorChargeStation" );
            modelAndView.getModel().put( "chargeStationErrorMsg", "Error in Deleting Charging Point - "+chargePoint.getReference() );
        }
        return modelAndView;

    }

    @RequestMapping(value = "/saveExistingChargeStation", method = RequestMethod.POST)
    public ModelAndView updateChargeStation(SecurityContextHolderAwareRequestWrapper request,@ModelAttribute("chargeStation" ) ChargeStationBean chargeStationBean )
    {
        User loggedUser = ( User)( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getPrincipal();
        ChargePoint chargePoint = new ChargePoint();
        chargePoint.init();
        chargeStationBean.decodeBeanToReal( chargePoint );
        chargePoint.setAdminUserName( loggedUser.getUsername() );
        chargePoint.setStatus( Savable.MODIFIED );
        ChgResponse chgResponse = CoreController.save( chargePoint );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChgResponse loadedChargePointResponse = ChargeStationLoader.loadSpecificChargePointByPointID( chargePoint.getChargePointId() );
            if (loadedChargePointResponse.isSuccess())
            {
                ChargePoint updatedChargePoint = (ChargePoint)loadedChargePointResponse.getReturnData();
                ChargeStationBean updateChargeStationBean = new ChargeStationBean();
                updateChargeStationBean.createBean( updatedChargePoint );
                modelAndView.setViewName( "chargeStation/loadChargeStation" );
                modelAndView.getModel().put( "chargeStation", updateChargeStationBean );
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/saveNewChargeStation", method = RequestMethod.POST)
    public ModelAndView saveNewLocation(SecurityContextHolderAwareRequestWrapper request,@ModelAttribute("chargeStation" ) ChargeStationBean chargeStationBean )
    {
        User loggedUser = ( User)( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getPrincipal();
        ChargePoint chargePoint = new ChargePoint();
        chargeStationBean.decodeBeanToReal( chargePoint );
        chargePoint.setStatus( Savable.NEW );
        chargePoint.setAdminUserName( loggedUser.getUsername() );
        ChgResponse chgResponse = CoreController.save( chargePoint );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChgResponse loadedChargePointResponse = ChargeStationLoader.loadSpecificChargePointByReference( chargePoint.getReference() );
            if (loadedChargePointResponse.isSuccess())
            {
                ChargePoint updatedChargePoint = (ChargePoint)loadedChargePointResponse.getReturnData();
                ChargeStationBean updateChargeStationBean = new ChargeStationBean();
                updateChargeStationBean.createBean( updatedChargePoint );
                modelAndView.setViewName( "chargeStation/loadChargeStation" );
                modelAndView.getModel().put( "chargeStation", updateChargeStationBean );
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/chargeStation/addChargeStation", method = RequestMethod.GET)
    public ModelAndView addLocation()
    {
        ModelAndView modelAndView = new ModelAndView();
        ChargeStationBean chargeStationBean = new ChargeStationBean();
        ChargePoint chargePoint = new ChargePoint();
        chargePoint.init();
        chargeStationBean.createBean( chargePoint );
        modelAndView.setViewName( "chargeStation/addChargeStation" );
        modelAndView.getModel().put( "chargeStation", chargeStationBean );
        ChgResponse chgLocationsResponse = LocationLoader.loadAllChargeLocations();
        if (chgLocationsResponse.isSuccess())
        {
            List<ChargeLocation> chargeLocations = (List)chgLocationsResponse.getReturnData();
            List locationBeanList = LocationBean.getBeanList( chargeLocations, DomainBeanImpl.LOCATION_BEAN_ID );
            modelAndView.getModel().put( "locations", locationBeanList );
        }
        ChgResponse chgPointStatusAvailabilityRes = ChargeStationAvailabilityStatusLoader.loadAllChargePointStatus();
        if( chgPointStatusAvailabilityRes.isSuccess() )
        {
            List<String> chargePointAvailabilityStatus = (List) chgPointStatusAvailabilityRes.getReturnData();
            List availabilityStatusBeanList = ChargeStationAvailabilityStatusBean.getBeanList( chargePointAvailabilityStatus, DomainBeanImpl.CHARGE_STATION_AVAILABILITY_STATUS_BEAN_ID );
//            for( ChargeStationAvailabilityStatusBean status : (List<ChargeStationAvailabilityStatusBean>)statusBeanList )
//            {
//                status.setSelected( status.getName().equals( chargeStationBean.getChargePointStatus() ) );
//            }
            modelAndView.getModel().put( "availabilityStatusList",availabilityStatusBeanList );
        }
        ChgResponse chgPointPowerStatusRes = ChargeStationPowerStatusLoader.loadAllChargePointPowerStatus();
        if( chgPointPowerStatusRes.isSuccess() )
        {
            List<String> chargePointPowerStatus = (List) chgPointPowerStatusRes.getReturnData();
            List powerStatusBeanList = ChargeStationPowerStatusBean.getBeanList( chargePointPowerStatus, DomainBeanImpl.CHARGE_STATION_POWER_STATUS_BEAN_ID );
            modelAndView.getModel().put( "powerStatusList",powerStatusBeanList );
        }
        ChgResponse chgPointProtocolsRes = ChargeStationProtocolLoader.loadAllChargePointProtocols();
        if( chgPointProtocolsRes.isSuccess() )
        {
            List<String> chargePointProtocols = (List) chgPointProtocolsRes.getReturnData();
            List protocolsBeanList = ChargeStationProtocolBean.getBeanList( chargePointProtocols, DomainBeanImpl.CHARGE_STATION_PROTOCOL_BEAN_ID );
            modelAndView.getModel().put( "protocolList",protocolsBeanList );
        }
        ChgResponse chgPointTypesRes = ChargeStationTypeLoader.loadAllChargePointTypes();
        if( chgPointTypesRes.isSuccess() )
        {
            List<String> chargePointTypes = (List) chgPointTypesRes.getReturnData();
            List typesBeanList = ChargeStationTypeBean.getBeanList( chargePointTypes, DomainBeanImpl.CHARGE_STATION_TYPE_BEAN_ID );
            modelAndView.getModel().put( "typeList",typesBeanList );
        }
        ChgResponse chgPointOwnersRes = ChgUserDataLoader.getUserListForSpecificRole( UserRoles.CHG_OWNER);
        if( chgPointOwnersRes.isSuccess() )
        {
            List<ChgUserBean> chargeOwnerBeans = (List) chgPointOwnersRes.getReturnData();
            modelAndView.getModel().put( "owners",chargeOwnerBeans );
        }
        return modelAndView;
    }
}
