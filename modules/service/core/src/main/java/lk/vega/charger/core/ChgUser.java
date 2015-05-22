package lk.vega.charger.core;

import lk.vega.charger.util.ChgTimeStamp;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.SavingSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/5/15.
 * Time on 11:59 AM
 */
public class ChgUser extends Savable
{
    public static final String INACTIVE_USER = "INACTIVE_USER";
    public static final String ACTIVE_USER = "ACTIVE_USER";
    public static final String BLOCKED_USER = "BLOCKED_USER";

    public static final String USER_CUSTOMER = "CHG_CUSTOMER";

    public static final String CREATED_BY_ONLINE = "ONLINE";
    public static final String CREATED_BY_ADMIN = "ADMIN";

    protected String userName;
    protected int status;
    private int userId;
    private String userType;
    private String userStatus;
    private ChgTimeStamp lastUpdateTimeStamp;
    private ChgTimeStamp createdTimeStamp;
    private String createdBy;



    public void setLastUpdateTimeStamp( ChgTimeStamp lastUpdateTimeStamp )
    {
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
    }

    public void setCreatedTimeStamp( ChgTimeStamp createdTimeStamp )
    {
        this.createdTimeStamp = createdTimeStamp;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

    public ChgTimeStamp getLastUpdateTimeStamp()
    {
        return lastUpdateTimeStamp;
    }

    public ChgTimeStamp getCreatedTimeStamp()
    {
        return createdTimeStamp;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType( String userType )
    {
        this.userType = userType;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId( int userId )
    {
        this.userId = userId;
    }

    public String getUserStatus()
    {
        return userStatus;
    }

    public void setUserStatus( String userStatus )
    {
        this.userStatus = userStatus;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }


    @Override
    public void save( Connection con ) throws SavingSQLException
    {
        String action = "";
        try
        {
            if( this.status == Savable.NEW )
            {
                action = "Inserting";
                checkValidity();
                insert( con );
            }
            else if( this.status == Savable.MODIFIED )
            {
                action = "Updating";
                checkValidity();
                update( con );
            }
            else if( this.status == Savable.DELETED )
            {
                action = "Deleting";
                checkValidity();
                delete( con );
            }
            else if( this.status != Savable.UNCHANGED )
            {
                throw new SavingSQLException( "Incorret setting of Status flag!" );
            }
        }
        catch( SQLException se )
        {
            se.printStackTrace();
            throw new SavingSQLException( "Error in " + action + se.getMessage(), se.getSQLState(), se.getErrorCode() );
        }
    }

    public void checkValidity() throws SavingSQLException
    {

    }

    protected void insert( Connection con ) throws SQLException
    {
        this.createdTimeStamp = new ChgTimeStamp(  );
        this.lastUpdateTimeStamp = new ChgTimeStamp(  );

        StringBuilder sb = new StringBuilder( "INSERT INTO CHG_USER ( " );
        sb.append( "USERNAME, " );
        sb.append( "TYPE, " );
        sb.append( "CREATED_DATE, " );
        sb.append( "MODIFIED_DATE, " );
        sb.append( "CREATED_BY, " );
        sb.append( "USER_STATUS " );
        sb.append( ") VALUES(?,?,?,?,?,?)" );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.userName );
            ps.setString( ++count, this.userType );
            if( this.createdTimeStamp == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, createdTimeStamp._getSQLTimestamp());
            }
            if( this.lastUpdateTimeStamp == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, lastUpdateTimeStamp._getSQLTimestamp() );
            }
            ps.setString( ++count, this.createdBy );
            ps.setString( ++count, this.userStatus );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }

    protected void update( Connection con ) throws SQLException
    {
        this.lastUpdateTimeStamp = new ChgTimeStamp(  );

        StringBuilder sb = new StringBuilder( "UPDATE CHG_USER SET " );
        sb.append( "USERNAME = ?, " );
        sb.append( "TYPE = ?, " );
        sb.append( "MODIFIED_DATE = ?, " );
        sb.append( "CREATED_BY = ?, " );
        sb.append( "USER_STATUS = ? " );
        sb.append( "WHERE " );
        sb.append( "USER_ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.userName );
            ps.setString( ++count, this.userType );
            if( this.lastUpdateTimeStamp == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, lastUpdateTimeStamp._getSQLTimestamp() );
            }
            ps.setString( ++count, this.createdBy );
            ps.setString( ++count, this.userStatus );
            ps.setInt( ++count, this.userId );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }

    protected void delete( Connection con ) throws SQLException
    {
        StringBuilder sb = new StringBuilder( "DELETE FROM CHG_USER WHERE " );
        sb.append( "USER_ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( ++count, this.userId );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }

    @Override
    public void load( ResultSet rs, Connection con, int level ) throws SQLException
    {
        this.status = Savable.UNCHANGED;
        this.userId = rs.getInt( "USER_ID" );
        this.userName = rs.getString( "USERNAME" );
        this.userType = rs.getString( "TYPE" );
        this.userStatus = rs.getString( "USER_STATUS" );
        this.createdBy = rs.getString( "CREATED_BY" );
        this.createdTimeStamp = new ChgTimeStamp( rs.getTimestamp( "CREATED_DATE" ) );
        this.lastUpdateTimeStamp = new ChgTimeStamp( rs.getTimestamp( "MODIFIED_DATE" ) );
    }

    public void init()
    {
        userName = null;
        userType = null;
        createdBy = null;
        userId = -1;
        userStatus = null;
        status = Savable.UNCHANGED;
    }
}
