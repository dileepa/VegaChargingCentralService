package lk.vega.charger.centralservice.client.web.controller.location;

import lk.vega.charger.centralservice.client.web.dataLoader.loacation.LocationLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.location.LocationBean;
import lk.vega.charger.core.ChargeLocation;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.Savable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/23/15.
 * Time on 2:37 PM
 */
@Controller
public class LocationsController
{
    @RequestMapping(value = "/AllLocations", method = RequestMethod.GET)
    public ModelAndView index()
    {
        ChgResponse chgResponse = LocationLoader.loadAllChargeLocations();
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            List<ChargeLocation> chargeLocations = (List)chgResponse.getReturnData();
            List locationBeanList = LocationBean.getBeanList( chargeLocations, DomainBeanImpl.LOCATION_BEAN_ID );
            modelAndView.setViewName( "location/locations" );
            modelAndView.getModel().put( "locations", locationBeanList );
        }
        return modelAndView;

    }


    @RequestMapping(value = "/location/addLocation", method = RequestMethod.GET)
    public ModelAndView addLocation()
    {
        ModelAndView modelAndView = new ModelAndView();
        LocationBean locationBean = new LocationBean();
        ChargeLocation chargeLocation = new ChargeLocation();
        chargeLocation.init();
        locationBean.createBean( chargeLocation );
        modelAndView.setViewName( "location/addLocation" );
        modelAndView.getModel().put( "location", locationBean );
        return modelAndView;
    }

    @RequestMapping(value = "/editLocation", method = RequestMethod.POST)
//    public ModelAndView editLocation(@RequestParam(value = "locationId", required = false ) Integer locationId )
    public ModelAndView editLocation(@ModelAttribute("location" )  LocationBean modelLocationBean )
    {
        ChgResponse chgResponse = LocationLoader.loadSpecificLocationByLocationID( modelLocationBean.getLocationId() );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChargeLocation chargeLocation = (ChargeLocation)chgResponse.getReturnData();
            LocationBean locationBean = new LocationBean();
            locationBean.createBean( chargeLocation );
            modelAndView.setViewName( "location/editLocation" );
            modelAndView.getModel().put( "location", locationBean );
        }
        return modelAndView;
    }

    @RequestMapping(value = "/saveExistingLocation", method = RequestMethod.POST)
    public ModelAndView updateLocation(@ModelAttribute("ChargeLocation" ) LocationBean locationBean )
    {
        ChargeLocation chargeLocation = new ChargeLocation();
        locationBean.decodeBeanToReal( chargeLocation );
        chargeLocation.setStatus( Savable.MODIFIED );
        ChgResponse chgResponse = CoreController.save( chargeLocation );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChgResponse loadedLocationResponse = LocationLoader.loadSpecificLocationByLocationID( chargeLocation.getLocationId() );
            if (loadedLocationResponse.isSuccess())
            {
                ChargeLocation updatedChargeLocation = (ChargeLocation)loadedLocationResponse.getReturnData();
                LocationBean updateLocationBean = new LocationBean();
                updateLocationBean.createBean( updatedChargeLocation );
                modelAndView.setViewName( "location/loadLocation" );
                modelAndView.getModel().put( "location", updateLocationBean );
            }
        }
        return modelAndView;
    }



    @RequestMapping(value = "/saveNewLocation", method = RequestMethod.POST)
    public ModelAndView saveNewLocation(@ModelAttribute("ChargeLocation" ) LocationBean locationBean )
    {
        ChargeLocation chargeLocation = new ChargeLocation();
        locationBean.decodeBeanToReal( chargeLocation );
        chargeLocation.setStatus( Savable.NEW );
        ChgResponse chgResponse = CoreController.save( chargeLocation );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChgResponse loadedLocationResponse = LocationLoader.loadSpecificLocationByLocationName( chargeLocation.getName() );
            if (loadedLocationResponse.isSuccess())
            {
                ChargeLocation updatedChargeLocation = (ChargeLocation)loadedLocationResponse.getReturnData();
                LocationBean updateLocationBean = new LocationBean();
                updateLocationBean.createBean( updatedChargeLocation );
                modelAndView.setViewName( "location/loadLocation" );
                modelAndView.getModel().put( "location", updateLocationBean );
            }
        }
        return modelAndView;
    }
}
