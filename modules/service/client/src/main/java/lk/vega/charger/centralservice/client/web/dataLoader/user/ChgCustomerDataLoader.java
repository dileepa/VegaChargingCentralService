package lk.vega.charger.centralservice.client.web.dataLoader.user;

import lk.vega.charger.core.ChgCustomerUser;
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
* Date on 5/21/15.
* Time on 8:34 PM
*/
public class ChgCustomerDataLoader
{
    public static ChgResponse loadCustomerListBySpecificSUserStatus(String Status)
    {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List chgCustomerUserList  = new ArrayList<ChgCustomerUser>(  );
        sb.append( "SELECT * FROM CHG_USER_CUSTOMER " );
        sb.append( "INNER JOIN " );
        sb.append( "CHG_USER " );
        sb.append( "ON " );
        sb.append( "CHG_USER_CUSTOMER.CUS_USERNAME = CHG_USER.USERNAME " );
        sb.append( "AND " );
        sb.append( "CHG_USER.TYPE = 'CHG_CUSTOMER' " );
        sb.append( "AND " );
        sb.append( "CHG_USER.USER_STATUS = ? " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, Status );
            rs = ps.executeQuery();
            while( rs.next() )
            {
                ChgCustomerUser chgCustomerUser = new ChgCustomerUser();
                chgCustomerUser.init();
                chgCustomerUser.load( rs, con, 0 );
                chgCustomerUserList.add( chgCustomerUser );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chgCustomerUserList);

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

    public static ChgResponse loadAllCustomerList()
    {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List chgCustomerUserList  = new ArrayList<ChgCustomerUser>(  );
        sb.append( "SELECT * FROM CHG_USER_CUSTOMER " );
        sb.append( "INNER JOIN " );
        sb.append( "CHG_USER " );
        sb.append( "ON " );
        sb.append( "CHG_USER_CUSTOMER.CUS_USERNAME = CHG_USER.USERNAME " );
        sb.append( "AND " );
        sb.append( "CHG_USER.TYPE = 'CHG_CUSTOMER' " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            rs = ps.executeQuery();
            while( rs.next() )
            {
                ChgCustomerUser chgCustomerUser = new ChgCustomerUser();
                chgCustomerUser.init();
                chgCustomerUser.load( rs, con, 0 );
                chgCustomerUserList.add( chgCustomerUser );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chgCustomerUserList);

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

    public static ChgResponse loadCustomerByUserID(int userId)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_USER_CUSTOMER " );
        sb.append( "INNER JOIN " );
        sb.append( "CHG_USER " );
        sb.append( "ON " );
        sb.append( "CHG_USER_CUSTOMER.CUS_USERNAME = CHG_USER.USERNAME " );
        sb.append( "AND " );
        sb.append( "CHG_USER.TYPE = 'CHG_CUSTOMER' " );
        sb.append( "AND " );
        sb.append( "CHG_USER.USER_ID = ? " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( 1,userId );
            rs = ps.executeQuery();
            if ( rs.next() )
            {
                ChgCustomerUser chgCustomerUser = new ChgCustomerUser();
                chgCustomerUser.init();
                chgCustomerUser.load( rs, con, 0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chgCustomerUser);
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

    public static ChgResponse loadCustomerByRFID(String ref)
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM CHG_USER_CUSTOMER " );
        sb.append( "INNER JOIN " );
        sb.append( "CHG_USER " );
        sb.append( "ON " );
        sb.append( "CHG_USER_CUSTOMER.CUS_USERNAME = CHG_USER.USERNAME " );
        sb.append( "AND " );
        sb.append( "CHG_USER.TYPE = 'CHG_CUSTOMER' " );
        sb.append( "AND " );
        sb.append( "CHG_USER_CUSTOMER.NFC_REF = ? " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1,ref );
            rs = ps.executeQuery();
            if ( rs.next() )
            {
                ChgCustomerUser chgCustomerUser = new ChgCustomerUser();
                chgCustomerUser.init();
                chgCustomerUser.load( rs, con, 0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chgCustomerUser);
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
