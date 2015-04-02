package lk.vega.charger.core;

import lk.vega.charger.util.ChgTimeStamp;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.SavingSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dileepa on 4/2/15.
 */
public class ChargeAuthKey extends Savable
{

    private int authID;
    private String authKey;
    private ChgTimeStamp startTime;
    private ChgTimeStamp endTime;
    private int status;

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public String getAuthKey()
    {
        return authKey;
    }

    public void setAuthKey( String authKey )
    {
        this.authKey = authKey;
    }

    public ChgTimeStamp getStartTime()
    {
        return startTime;
    }

    public void setStartTime( ChgTimeStamp startTime )
    {
        this.startTime = startTime;
    }

    public ChgTimeStamp getEndTime()
    {
        return endTime;
    }

    public void setEndTime( ChgTimeStamp endTime )
    {
        this.endTime = endTime;
    }

    public int getAuthID()
    {
        return authID;
    }

    public void setAuthID( int authID )
    {
        this.authID = authID;
    }

    public void checkValidity() throws SavingSQLException
    {

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

    @Override
    public void load( ResultSet rs, Connection con, int level ) throws SQLException
    {

        this.status = Savable.UNCHANGED;
        this.authID = rs.getInt( "AUTH_ID" );
        this.authKey = rs.getString( "AUTH_KEY" );
        this.startTime = new ChgTimeStamp( rs.getTimestamp( "AUTH_START" ) );
        this.endTime = new ChgTimeStamp( rs.getTimestamp( "AUTH_STOP" ) );

    }

    public void init()
    {
        this.authID = -1;
        this.authKey = null;
        this.startTime = null;
        this.endTime = null;
        this.status = Savable.UNCHANGED;
    }

    private void update( Connection con ) throws SQLException
    {

        StringBuilder sb = new StringBuilder( "UPDATE TRS_AUTH_KEY_STORE SET " );
        sb.append( "AUTH_KEY = ?, " );
        sb.append( "AUTH_START = ?, " );
        sb.append( "AUTH_STOP = ? " );
        sb.append( "WHERE " );
        sb.append( "AUTH_ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.authKey );
            ps.setTimestamp( ++count, startTime._getSQLTimestamp() );
            ps.setTimestamp( ++count, endTime._getSQLTimestamp() );
            ps.setInt( ++count, this.authID );

            ps.execute();
            ps.close();
        }
        finally
        {

            DBUtility.close( ps );
        }
    }

    private void delete( Connection con ) throws SQLException
    {
        StringBuilder sb = new StringBuilder( "DELETE FROM TRS_AUTH_KEY_STORE WHERE " );
        sb.append( "AUTH_ID = ? " );

        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( ++count, this.authID );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }

    private void insert( Connection con ) throws SQLException
    {

        StringBuilder sb = new StringBuilder( "INSERT INTO TRS_AUTH_KEY_STORE ( " );
        sb.append( "AUTH_KEY, " );
        sb.append( "AUTH_START, " );
        sb.append( "AUTH_STOP " );
        sb.append( ") VALUES(?,?,?)");
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.authKey );
            ps.setTimestamp( ++count, startTime._getSQLTimestamp() );
            ps.setTimestamp( ++count, endTime._getSQLTimestamp() );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }



}
