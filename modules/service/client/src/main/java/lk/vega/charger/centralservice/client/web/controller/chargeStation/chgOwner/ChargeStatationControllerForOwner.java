package lk.vega.charger.centralservice.client.web.controller.chargeStation.chgOwner;

import lk.vega.charger.centralservice.client.web.controller.chargeStation.ChargeStationsController;
import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;
import lk.vega.charger.centralservice.client.web.domain.user.User;
import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.util.ChgResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/12/15.
 * Time on 1:09 PM
 */
@Controller
public class ChargeStatationControllerForOwner
{
    @RequestMapping(value = "/OwnerSpecificChargeStations", method = RequestMethod.GET)
    public ModelAndView viewOwnerSpecificChargeStations(SecurityContextHolderAwareRequestWrapper request)
    {
        User loggedUser = ( User)( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getPrincipal();
        ChgResponse chgResponse = ChargeStationLoader.loadOwnerSpecificChargePoints( loggedUser.getUsername() );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            List<ChargePoint> allChargePointList = (List)chgResponse.getReturnData();
            List allChargePointBeanList = ChargeStationBean.getBeanList( allChargePointList, DomainBeanImpl.CHARGE_STATION_BEAN_ID );
            modelAndView.setViewName( "user/chgOwner/chargeStations" );
            modelAndView.getModel().put( "chgStations", allChargePointBeanList );
        }
        return modelAndView;
    }

    @RequestMapping(value = "/user/chgOwner/editChargeStation", method = RequestMethod.GET)
    public ModelAndView editOwnerSpecificPoint(@RequestParam(value = "chgStationID", required = false ) Integer chgStationID )
    {
        ChargeStationsController chargeStationsController = new ChargeStationsController();
        ModelAndView modelAndView = chargeStationsController.editLocation( chgStationID );
        modelAndView.setViewName( "user/chgOwner/editChargeStation" );
        return modelAndView;
    }

    @RequestMapping(value = "/saveExistingOwnerSpecificChargeStation", method = RequestMethod.POST)
    public ModelAndView updateOwnerSpecificChargeStation(SecurityContextHolderAwareRequestWrapper request,@ModelAttribute("chargeStation" ) ChargeStationBean chargeStationBean )
    {
        ChargeStationsController chargeStationsController = new ChargeStationsController();
        ModelAndView modelAndView = chargeStationsController.updateChargeStation( request,chargeStationBean );
        modelAndView.setViewName( "user/chgOwner/loadChargeStation" );
        return modelAndView;
    }

    @RequestMapping(value = "/user/chgOwner/addChargeStation", method = RequestMethod.GET)
    public ModelAndView addLocation()
    {
        ChargeStationsController chargeStationsController = new ChargeStationsController();
        ModelAndView modelAndView = chargeStationsController.addLocation();
        modelAndView.setViewName( "user/chgOwner/addChargeStation" );
        return modelAndView;
    }

    @RequestMapping(value = "/saveNewOwnerSpecificChargeStation", method = RequestMethod.POST)
    public ModelAndView saveNewOwnerSpecificChargeStation(SecurityContextHolderAwareRequestWrapper request,@ModelAttribute("chargeStation" ) ChargeStationBean chargeStationBean )
    {
        ChargeStationsController chargeStationsController = new ChargeStationsController();
        ModelAndView modelAndView = chargeStationsController.saveNewLocation(request,chargeStationBean);
        modelAndView.setViewName( "user/chgOwner/loadChargeStation" );
        return modelAndView;
    }
}
