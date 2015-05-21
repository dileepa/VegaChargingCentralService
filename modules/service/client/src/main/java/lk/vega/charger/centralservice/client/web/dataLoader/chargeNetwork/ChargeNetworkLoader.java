package lk.vega.charger.centralservice.client.web.dataLoader.chargeNetwork;

import lk.vega.charger.core.ChargeNetwork;
import lk.vega.charger.core.ChargeNetworkAndStationMapping;
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
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/15/15.
 * Time on 3:19 PM
 */
public class ChargeNetworkLoader
{

    public static ChgResponse loadSpecificChargeNetworkByReference( String refName )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_NETWORK WHERE NETWORK_REFERENCE = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, refName );
            rs = ps.executeQuery();
            if (rs.next())
            {
                ChargeNetwork chargeNetwork = new ChargeNetwork();
                chargeNetwork.init();
                chargeNetwork.load( rs, con, 0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargeNetwork);
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

    public static ChgResponse loadAllChargeNetworks(  )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List chargeNetworkList = new ArrayList<ChargeNetwork>(  );
        sb.append( "SELECT * FROM CHG_NETWORK " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            rs = ps.executeQuery();
            while( rs.next() )
            {
                ChargeNetwork chargeNetwork = new ChargeNetwork();
                chargeNetwork.init();
                chargeNetwork.load( rs, con, 0 );
                chargeNetworkList.add( chargeNetwork );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargeNetworkList);

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

    public static ChgResponse loadSpecificChargeNetworkById( int networkId )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_NETWORK WHERE NETWORK_ID = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( 1, networkId );
            rs = ps.executeQuery();
            if (rs.next())
            {
                ChargeNetwork chargeNetwork = new ChargeNetwork();
                chargeNetwork.init();
                chargeNetwork.load( rs, con, 0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargeNetwork);
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


    public static ChgResponse loadNetworkSpecificStationIds( int networkId )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List chargeNetworkAndStationMappingList  = new ArrayList<ChargeNetworkAndStationMapping>(  );
        sb.append( "SELECT * FROM CHG_NETWORK_STATION_MAP WHERE CHG_NETWORK_ID = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( 1, networkId );
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

    public static ChgResponse loadSpecificNetworksByIds( String commaSeparatedNetworkIds )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List chargeNetworks  = new ArrayList<ChargeNetwork>(  );
        sb.append( "SELECT * FROM CHG_NETWORK WHERE NETWORK_ID IN (" );
        sb.append( commaSeparatedNetworkIds );
        sb.append( ")" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            rs = ps.executeQuery();
            while( rs.next() )
            {
                ChargeNetwork chargeNetwork = new ChargeNetwork();
                chargeNetwork.init();
                chargeNetwork.load( rs, con, 0 );
                chargeNetworks.add( chargeNetwork );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargeNetworks);

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
