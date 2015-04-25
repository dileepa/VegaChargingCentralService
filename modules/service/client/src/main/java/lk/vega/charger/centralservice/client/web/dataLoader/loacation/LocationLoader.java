package lk.vega.charger.centralservice.client.web.dataLoader.loacation;

import lk.vega.charger.core.ChargeLocation;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.connection.CHGConnectionPoolFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * USER : Dileepa
 * DATE : 4/25/15
 * TIME : 3:20 PM
 */
public class LocationLoader
{

    public static  ChgResponse loadSpecificLocationByLocationName( String name )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_POINT_LOCATION WHERE LOCATION = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, name );
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



    public static  ChgResponse loadSpecificLocationByLocationID( int locationId )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_POINT_LOCATION WHERE CHG_POINT_LOCATION_ID = ?" );
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

    public static ChgResponse loadAllChargeLocations()
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List<ChargeLocation>  chargeLocations = new ArrayList<ChargeLocation>(  );
        sb.append( "SELECT * FROM CHG_POINT_LOCATION " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool(CHGConnectionPoolFactory.MYSQL) ).getConnection();
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
            DBUtility.close(rs);
            DBUtility.close( ps );
            DBUtility.close( con );
        }
    }

    public static ChargeLocation loadSpecificElementByPrimaryKey(int id)
    {
        return null;
    }
}
