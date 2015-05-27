package lk.vega.charger.centralservice.client.web.dataLoader.chargeStation;

import lk.vega.charger.core.ChargePointTransactionStatus;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.connection.CHGConnectionPoolFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/27/15.
 * Time on 3:49 PM
 */
public class ChargePointTransactionStatusLoader
{

    public static ChgResponse loadSpecificChargePointTransactionStatusByReference( String chgPointRef )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_POINT_TRS_STATUS WHERE CHG_POINT_REF = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, chgPointRef );
            rs = ps.executeQuery();
            if (rs.next())
            {
                ChargePointTransactionStatus chargePointTransactionStatus = new ChargePointTransactionStatus();
                chargePointTransactionStatus.init();
                chargePointTransactionStatus.load( rs, con, 0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargePointTransactionStatus);
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
