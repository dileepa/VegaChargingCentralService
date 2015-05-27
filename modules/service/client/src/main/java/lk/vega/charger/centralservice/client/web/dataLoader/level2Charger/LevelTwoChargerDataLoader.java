package lk.vega.charger.centralservice.client.web.dataLoader.level2Charger;

import lk.vega.charger.core.ChgLevelTwoTransaction;
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
 * Time on 3:16 PM
 */
public class LevelTwoChargerDataLoader
{

    public static ChgResponse loadSpecificChargePointByPointID( String trsId, String trsStatus )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM TRS_LEVEL_TWO_CHARGER WHERE TRS_ID = ? " );
        sb.append( "AND " );
        sb.append( "TRS__STATUS = ? " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, trsId );
            ps.setString( 2, trsStatus );
            rs = ps.executeQuery();
            if (rs.next())
            {
                ChgLevelTwoTransaction chgLevelTwoTransaction = new ChgLevelTwoTransaction();
                chgLevelTwoTransaction.init();
                chgLevelTwoTransaction.load( rs, con, 0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chgLevelTwoTransaction);
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

    public static ChgResponse loadAllTransactions( )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List loadAllUserSpecificTrs = new ArrayList<ChgLevelTwoTransaction>(  );
        sb.append( "SELECT * FROM TRS_LEVEL_TWO_CHARGER " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            rs = ps.executeQuery();
            while(rs.next())
            {
                ChgLevelTwoTransaction chgLevelTwoTransaction = new ChgLevelTwoTransaction();
                chgLevelTwoTransaction.init();
                chgLevelTwoTransaction.load( rs, con, 0 );
                loadAllUserSpecificTrs.add( chgLevelTwoTransaction );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", loadAllUserSpecificTrs);

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

    public static ChgResponse loadChgPointOwnerSpecificTransactions( String ChargeOwnerName)

    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List loadAllUserSpecificTrs = new ArrayList<ChgLevelTwoTransaction>(  );
        sb.append( "SELECT * FROM TRS_LEVEL_TWO_CHARGER WHERE CHG_POINT_OWNER = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, ChargeOwnerName );
            rs = ps.executeQuery();
            while(rs.next())
            {
                ChgLevelTwoTransaction chgLevelTwoTransaction = new ChgLevelTwoTransaction();
                chgLevelTwoTransaction.init();
                chgLevelTwoTransaction.load( rs, con, 0 );
                loadAllUserSpecificTrs.add( chgLevelTwoTransaction );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", loadAllUserSpecificTrs);

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


    public static ChgResponse loadCustomerSpecificTransactions( String customerName)

    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List loadAllUserSpecificTrs = new ArrayList<ChgLevelTwoTransaction>(  );
        sb.append( "SELECT * FROM TRS_LEVEL_TWO_CHARGER WHERE CUSTOMER_USERNAME = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, customerName );
            rs = ps.executeQuery();
            while(rs.next())
            {
                ChgLevelTwoTransaction chgLevelTwoTransaction = new ChgLevelTwoTransaction();
                chgLevelTwoTransaction.init();
                chgLevelTwoTransaction.load( rs, con, 0 );
                loadAllUserSpecificTrs.add( chgLevelTwoTransaction );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", loadAllUserSpecificTrs);

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

    public static ChgResponse loadChgNetworkSpecificTransactions( String networkOwnerName)

    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List loadAllUserSpecificTrs = new ArrayList<ChgLevelTwoTransaction>(  );
        sb.append( "SELECT * FROM TRS_LEVEL_TWO_CHARGER WHERE NETWORK_OWNER = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, networkOwnerName );
            rs = ps.executeQuery();
            while(rs.next())
            {
                ChgLevelTwoTransaction chgLevelTwoTransaction = new ChgLevelTwoTransaction();
                chgLevelTwoTransaction.init();
                chgLevelTwoTransaction.load( rs, con, 0 );
                loadAllUserSpecificTrs.add( chgLevelTwoTransaction );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", loadAllUserSpecificTrs);

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
