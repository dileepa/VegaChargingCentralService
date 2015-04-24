package lk.vega.charger.centralservice.client.web.controller.chargeStation;

import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;
import lk.vega.charger.core.ChargeLocation;
import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.connection.CHGConnectionPoolFactory;
import org.springframework.stereotype.Controller;
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
 * Date on 4/24/15.
 * Time on 1:12 PM
 */
@Controller
public class ChargeStationsController
{
    @RequestMapping(value = "/AllChargeStations", method = RequestMethod.GET)
    public ModelAndView index()
    {
        ChgResponse chgResponse = loadAllChargePoints();
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

    public  ChgResponse loadAllChargePoints()
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List<ChargePoint>  userSpecificChargePointList = new ArrayList<ChargePoint>(  );
        sb.append( "SELECT * FROM CHG_POINT " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            rs = ps.executeQuery();
            while(rs.next())
            {
                ChargePoint chargePoint = new ChargePoint();
                chargePoint.init();
                chargePoint.load( rs, con, 11 );
                userSpecificChargePointList.add( chargePoint );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", userSpecificChargePointList);

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

    @RequestMapping(value = "/chargeStation/editChargeStation", method = RequestMethod.GET)
    public ModelAndView editLocation(@RequestParam(value = "chgStationID", required = false ) Integer chgStationID )
    {
        ChgResponse chgResponse = loadSpecificChargePointByPointID( chgStationID );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChargePoint chargePoint = (ChargePoint)chgResponse.getReturnData();
            ChargeStationBean chargeStationBean = new ChargeStationBean();
            chargeStationBean.createBean( chargePoint );
            modelAndView.setViewName( "chargeStation/editChargeStation" );
            modelAndView.getModel().put( "chargeStation", chargeStationBean );
        }
        return modelAndView;
    }

    private  ChgResponse loadSpecificChargePointByPointID( int chgStationID )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_POINT WHERE ID = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( 1, chgStationID );
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

}
