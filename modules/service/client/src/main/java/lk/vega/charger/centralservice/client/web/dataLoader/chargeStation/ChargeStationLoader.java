package lk.vega.charger.centralservice.client.web.dataLoader.chargeStation;

import lk.vega.charger.core.ChargeNetworkAndStationMapping;
import lk.vega.charger.core.ChargePoint;
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
 * TIME : 3:28 PM
 */
public class ChargeStationLoader
{

    public static  ChgResponse loadSpecificChargePointByPointID( int chgStationID )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_POINT WHERE CHG_POINT_ID = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( 1, chgStationID );
            rs = ps.executeQuery();
            if (rs.next())
            {
                ChargePoint chargePoint = new ChargePoint();
                chargePoint.init();
                chargePoint.load( rs, con, 0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargePoint);
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

    public static ChgResponse loadAllChargePoints()
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List<ChargePoint> userSpecificChargePointList = new ArrayList<ChargePoint>(  );
        sb.append( "SELECT * FROM CHG_POINT " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool(CHGConnectionPoolFactory.MYSQL) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            rs = ps.executeQuery();
            while(rs.next())
            {
                ChargePoint chargePoint = new ChargePoint();
                chargePoint.init();
                chargePoint.load( rs, con, 0 );
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
            DBUtility.close(rs);
            DBUtility.close( ps );
            DBUtility.close( con );
        }
    }

    public static  ChgResponse loadSpecificChargePointByReference( String refName )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_POINT WHERE REFERENCE = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, refName );
            rs = ps.executeQuery();
            if (rs.next())
            {
                ChargePoint chargePoint = new ChargePoint();
                chargePoint.init();
                chargePoint.load( rs, con, 0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargePoint);
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

    public static ChgResponse loadOwnerSpecificChargePoints( String username )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List<ChargePoint> userSpecificChargePointList = new ArrayList<ChargePoint>(  );
        sb.append( "SELECT * FROM CHG_POINT WHERE USERNAME = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool(CHGConnectionPoolFactory.MYSQL) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, username );
            rs = ps.executeQuery();
            while(rs.next())
            {
                ChargePoint chargePoint = new ChargePoint();
                chargePoint.init();
                chargePoint.load( rs, con, 0 );
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
            DBUtility.close(rs);
            DBUtility.close( ps );
            DBUtility.close( con );
        }

    }

    public static ChgResponse loadChargeStationSpecificNetworkIds( int chargePointId )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List chargeNetworkAndStationMappingList  = new ArrayList<ChargeNetworkAndStationMapping>(  );
        sb.append( "SELECT * FROM CHG_NETWORK_STATION_MAP WHERE CHG_STATION_ID = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( 1, chargePointId );
            rs = ps.executeQuery();
            while( rs.next() )
            {
                ChargeNetworkAndStationMapping chargeNetworkAndStationMapping = new ChargeNetworkAndStationMapping();
                chargeNetworkAndStationMapping.init();
                chargeNetworkAndStationMapping.load( rs, con, 0 );
                chargeNetworkAndStationMappingList.add( chargeNetworkAndStationMapping );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargeNetworkAndStationMappingList);

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


    public static ChgResponse loadSpecificStationsByIds( String commaSeparatedStationIds )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List chargePoints  = new ArrayList<ChargePoint>(  );
        sb.append( "SELECT * FROM CHG_POINT WHERE CHG_POINT_ID IN (" );
        sb.append( commaSeparatedStationIds );
        sb.append( ")" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            rs = ps.executeQuery();
            while( rs.next() )
            {
                ChargePoint chargePoint = new ChargePoint();
                chargePoint.init();
                chargePoint.load( rs, con, 0 );
                chargePoints.add( chargePoint );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargePoints);

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
