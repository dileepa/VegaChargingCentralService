package lk.vega.charger.centralservice.client.web.controller.location;

import lk.vega.charger.core.ChargeLocation;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.connection.CHGConnectionPoolFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        ChgResponse chgResponse = loadAllChargeLocations();
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            List<ChargeLocation> chargeLocations = (List)chgResponse.getReturnData();
            modelAndView.setViewName( "location/locations" );
            modelAndView.getModel().put( "locations", chargeLocations );
        }
        return modelAndView;

    }

    private  ChgResponse loadAllChargeLocations()
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List<ChargeLocation>  chargeLocations = new ArrayList<ChargeLocation>(  );
        sb.append( "SELECT * FROM CHG_POINT_LOCATION " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            rs = ps.executeQuery();
            while(rs.next())
            {
                ChargeLocation chargeLocation = new ChargeLocation();
                chargeLocation.init();
                chargeLocation.load( rs, con, 0 );
                chargeLocations.add( chargeLocation );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargeLocations);

        }
        catch( SQLException e )
        {
            e.printStackTrace();
            return new ChgResponse( ChgResponse.ERROR, e.getMessage() );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            return new ChgResponse( ChgResponse.ERROR, e.getMessage());
        }
        finally
        {
            DBUtility.close( rs );
            DBUtility.close( ps );
            DBUtility.close( con );
        }
    }

    @RequestMapping(value = "/location/editLocation", method = RequestMethod.GET)
    public ModelAndView editLocation(@RequestParam(value = "locationID", required = false ) Integer locationId )
    {
        ChgResponse chgResponse = loadSpecificLocation( locationId );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChargeLocation chargeLocation = (ChargeLocation)chgResponse.getReturnData();
            modelAndView.setViewName( "location/editLocation" );
            modelAndView.getModel().put( "location", chargeLocation );
        }
        return modelAndView;
    }

    private  ChgResponse loadSpecificLocation( int locationId )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_POINT_LOCATION WHERE ID = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( 1, locationId );
            rs = ps.executeQuery();
            if (rs.next())
            {
                ChargeLocation chargeLocation = new ChargeLocation();
                chargeLocation.init();
                chargeLocation.load( rs, con, 0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargeLocation);
            }

        }
        catch( SQLException e )
        {
            e.printStackTrace();
            return new ChgResponse( ChgResponse.ERROR, e.getMessage() );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            return new ChgResponse( ChgResponse.ERROR, e.getMessage());
        }
        finally
        {
            DBUtility.close( rs );
            DBUtility.close( ps );
            DBUtility.close( con );
        }
        return new ChgResponse( ChgResponse.ERROR, "UNKNOWN ERROR" );
    }

    @RequestMapping(value = "/saveLocation", method = RequestMethod.POST)
    public ModelAndView saveLocation(@ModelAttribute("ChargeLocation" ) ChargeLocation chargeLocation )
    {
        chargeLocation.setStatus( Savable.MODIFIED );
        ChgResponse chgResponse = CoreController.save( chargeLocation );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChgResponse loadedLocationResponse = loadSpecificLocation( chargeLocation.getLocationId() );
            if (loadedLocationResponse.isSuccess())
            {
                ChargeLocation updatedChargeLocation = (ChargeLocation)loadedLocationResponse.getReturnData();
                modelAndView.setViewName( "location/loadLocation" );
                modelAndView.getModel().put( "location", updatedChargeLocation );
            }
        }
        return modelAndView;
    }
}
