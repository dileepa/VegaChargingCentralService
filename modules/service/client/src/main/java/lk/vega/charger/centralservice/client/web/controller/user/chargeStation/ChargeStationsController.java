package lk.vega.charger.centralservice.client.web.controller.user.chargeStation;

import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.connection.CHGConnectionPoolFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
 * Time on 9:46 AM
 */
@Controller
public class ChargeStationsController
{
    @RequestMapping(value = "/UserChargeStations", method = RequestMethod.GET)
    public ModelAndView index()
    {
        ChgResponse chgResponse = loadUserSpecificChargePoints();
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            List<ChargePoint>  userSpecificChargePointList = (List)chgResponse.getReturnData();
            modelAndView.setViewName( "user/chargeStation/chargeStations" );
            modelAndView.getModel().put( "chgStations", userSpecificChargePointList );
        }
        return modelAndView;
    }

    public  ChgResponse loadUserSpecificChargePoints( )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List<ChargePoint>  userSpecificChargePointList = new ArrayList<ChargePoint>(  );
        sb.append( "SELECT * FROM CHG_POINT WHERE USERID =? " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( 1, 1 );    //TODO set specific user id
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

}
