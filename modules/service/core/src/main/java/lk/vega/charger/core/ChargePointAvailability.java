package lk.vega.charger.core;

/**
 * Created by dileepa on 3/19/15.
 */

import lk.vega.charger.util.ChgTimeStamp;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.SavingSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ChargePointAvailability extends Savable
{

    public static final String OCCUPIED = "OCCUPIED";
    public static final String FREE = "FREE";
    public static final String IN_MAINTENANCE = "IN_MAINTENANCE";

    private int id;
    private int pointId;
    private ChgTimeStamp time;
    private String availability;
    private int status;

    public String getAvailability()
    {
        return availability;
    }

    public void setAvailability( String availability )
    {
        this.availability = availability;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public int getPointId()
    {
        return pointId;
    }

    public void setPointId( int pointId )
    {
        this.pointId = pointId;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public ChgTimeStamp getTime()
    {
        return time;
    }

    public void setTime( ChgTimeStamp time )
    {
        this.time = time;
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

    private void insert( Connection con ) throws SQLException
    {

        StringBuilder sb = new StringBuilder( "INSERT INTO TRS_CHG_POINT_STATUS ( " );
        sb.append( "POINTID, " );
        sb.append( "TIMESTAMP, " );
        sb.append( "STATUS " );
        sb.append( ") VALUES(?,?,?)");
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            if( this.id != -1 )
            {
                ps.setInt( ++count, this.id );
            }
            else
            {
                ps.setNull( ++count, java.sql.Types.NUMERIC );
            }
            ps.setInt( ++count, this.pointId );
            if( this.time == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, time._getSQLTimestamp() );
            }
            ps.setString( ++count, this.availability );
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
        this.id = rs.getInt( "TRS_CHG_POINT_STATUS_ID" );
        this.pointId = rs.getInt( "POINTID" );
        this.time = new ChgTimeStamp( rs.getTimestamp( "TIMESTAMP" ) );
        this.availability = rs.getString( "STATUS" );
    }

    private void update( Connection con ) throws SQLException
    {

        StringBuilder sb = new StringBuilder( "UPDATE TRS_CHG_POINT_STATUS SET " );
        sb.append( "POINTID = ?, " );
        sb.append( "TIMESTAMP = ?, " );
        sb.append( "STATUS = ? " );
        sb.append( "WHERE " );
        sb.append( "TRS_CHG_POINT_STATUS_ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( ++count, this.pointId );
            if( this.time == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, time._getSQLTimestamp() );
            }

            ps.setString( ++count, this.availability );
            ps.setInt( ++count, this.id );
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
        StringBuilder sb = new StringBuilder( "DELETE FROM TRS_CHG_POINT_STATUS WHERE " );
        sb.append( "TRS_CHG_POINT_STATUS_ID = ? " );

        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setLong( ++count, this.id );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }

    public void init()
    {
        this.id = -1;
        this.pointId = -1;
        this.time = null;
        this.availability = null;
        this.status = Savable.UNCHANGED;
    }

}
