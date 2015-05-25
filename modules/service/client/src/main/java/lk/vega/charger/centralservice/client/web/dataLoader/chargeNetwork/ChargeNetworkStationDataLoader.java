package lk.vega.charger.centralservice.client.web.dataLoader.chargeNetwork;

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
 * Date on 5/25/15.
 * Time on 11:39 AM
 */
public class ChargeNetworkStationDataLoader
{
    public static ChgResponse loadNetworksBySpecificChargePoints( int networkId )
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

}
