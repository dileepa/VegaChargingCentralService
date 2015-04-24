package lk.vega.charger.core;

/**
 * Created by dileepa on 3/19/15.
 */

import lk.vega.charger.util.*;
import lk.vega.charger.util.ChgDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ChargePoint extends Savable
{
    private int chargePointId;
    private String reference;
    private int locationId;
    private double power;
    private String type;
    private String protocol;
    private String version;
    private ChgDate lastUpdateDate;
    private ChgTimeStamp lastUpdateTimeStamp;
    private int userId;
    private ChargeLocation chargeLocation;
    private String machineUniqueRef;
    private int status;

    public String getMachineUniqueRef()
    {
        return machineUniqueRef;
    }

    public void setMachineUniqueRef( String machineUniqueRef )
    {
        this.machineUniqueRef = machineUniqueRef;
    }

    public ChargeLocation getChargeLocation()
    {
        return chargeLocation;
    }

    public void setChargeLocation( ChargeLocation chargeLocation )
    {
        this.chargeLocation = chargeLocation;
    }

    public int getChargePointId()
    {
        return chargePointId;
    }

    public void setChargePointId( int chargePointId )
    {
        this.chargePointId = chargePointId;
    }

    public ChgDate getLastUpdateDate()
    {
        return lastUpdateDate;
    }

    public void setLastUpdateDate( ChgDate lastUpdateDate )
    {
        this.lastUpdateDate = lastUpdateDate;
    }

    public ChgTimeStamp getLastUpdateTimeStamp()
    {
        return lastUpdateTimeStamp;
    }

    public void setLastUpdateTimeStamp( ChgTimeStamp lastUpdateTimeStamp )
    {
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
    }

    public int getLocationId()
    {
        return locationId;
    }

    public void setLocationId( int locationId )
    {
        this.locationId = locationId;
    }

    public double getPower()
    {
        return power;
    }

    public void setPower( double power )
    {
        this.power = power;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol( String protocol )
    {
        this.protocol = protocol;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference( String reference )
    {
        this.reference = reference;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId( int userId )
    {
        this.userId = userId;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
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

        //TODO Generate Squence Id here
        //        PreparedStatement ps1 = con.prepareStatement( "SELECT NAME oF SEQ.NEXTVAL FROM DUAL" );
        //        ResultSet rs1 = ps1.executeQuery();
        //        if( rs1.next() )
        //        {
        //            this.xxx = rs1.getLong( "NEXTVAL" );
        //        }
        //        DBUtility.close( rs1 );
        //        DBUtility.close( ps1 );

        this.lastUpdateDate = new ChgDate( );
        this.lastUpdateTimeStamp = new ChgTimeStamp( );

        StringBuilder sb = new StringBuilder( "INSERT INTO CHG_POINT ( " );
        sb.append( "ID, " );
        sb.append( "REFERENCE, " );
        sb.append( "LOCATIONID, " );
        sb.append( "POWER, " );
        sb.append( "TYPE, " );
        sb.append( "PROTOCOL, " );
        sb.append( "VERSION, " );
        sb.append( "LASTUPDATE, " );
        sb.append( "LASTUPDATETIMESTAMP, " );
        sb.append( "MACHINE_UNIQUE_ADDRESS, " );
        sb.append( "USERID " );
        sb.append( ") VALUES(?,?,?,?,?,?,?,?,?,?,?");
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            if( this.chargePointId != -1 )
            {
                ps.setInt( ++count, this.chargePointId );
            }
            else
            {
                ps.setNull( ++count, java.sql.Types.NUMERIC );
            }
            if( this.reference != null )
            {
                ps.setString( ++count, this.reference );
            }
            else
            {
                ps.setNull( ++count, Types.VARCHAR );
            }
            ps.setInt( ++count, this.locationId );
            ps.setDouble( ++count, this.power );
            ps.setString( ++count, this.type );
            ps.setString( ++count, this.protocol );
            ps.setString( ++count, this.version );
            if( this.lastUpdateDate == null )
            {
                ps.setNull( ++count, java.sql.Types.DATE );
            }
            else
            {
                ps.setDate( ++count, lastUpdateDate._getSQLDate() );
            }
            if( this.lastUpdateTimeStamp == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, lastUpdateTimeStamp._getSQLTimestamp());
            }
            ps.setString( ++count, this.machineUniqueRef );
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
        this.chargePointId = rs.getInt( "ID" );
        this.reference = rs.getString( "REFERENCE" );
        this.locationId = rs.getInt( "LOCATIONID" );
        this.power = rs.getDouble( "POWER" );
        this.type = rs.getString( "TYPE" );
        this.protocol = rs.getString( "PROTOCOL" );
        this.version = rs.getString( "VERSION" );
        this.lastUpdateDate = new ChgDate( rs.getDate( "LASTUPDATE" ) );
        this.machineUniqueRef = rs.getString( "MACHINE_UNIQUE_ADDRESS" );
        this.lastUpdateTimeStamp = new ChgTimeStamp( rs.getTimestamp( "LASTUPDATETIMESTAMP" ) );
        this.userId = rs.getInt( "USERID" );
        if( level > 10 )
        {
            PreparedStatement psChargeLocation = null;
            ResultSet rsChargeLocation = null;

            try
            {
                StringBuilder chargeLocationString = new StringBuilder( "SELECT * FROM CHG_POINT_LOCATION WHERE " );
                chargeLocationString.append( "ID = ? " );

                psChargeLocation = con.prepareStatement( chargeLocationString.toString() );
                psChargeLocation.setInt( 1, locationId );
                rsChargeLocation = psChargeLocation.executeQuery();
                if( rsChargeLocation.next() )
                {
                    ChargeLocation chargeLocation = new ChargeLocation();
                    chargeLocation.init();
                    chargeLocation.load( rsChargeLocation, con, 0 );
                    this.chargeLocation = chargeLocation;
                }
            }
            finally
            {
                DBUtility.close( rsChargeLocation );
                DBUtility.close( psChargeLocation );
            }

        }
    }

    private void update( Connection con ) throws SQLException
    {
        this.lastUpdateDate = new ChgDate( );
        this.lastUpdateTimeStamp = new ChgTimeStamp( );

        StringBuilder sb = new StringBuilder( "UPDATE CHG_POINT SET " );
        sb.append( "REFERENCE = ?, " );
        sb.append( "LOCATIONID = ?, " );
        sb.append( "POWER = ?, " );
        sb.append( "TYPE = ?, " );
        sb.append( "PROTOCOL = ?, " );
        sb.append( "VERSION = ?, " );
        sb.append( "LASTUPDATE = ?, " );
        sb.append( "LASTUPDATETIMESTAMP = ?, " );
        sb.append( "MACHINE_UNIQUE_ADDRESS = ?, " );
        sb.append( "USERID = ? " );
        sb.append( "WHERE " );
        sb.append( "ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            if( this.reference != null )
            {
                ps.setString( ++count, this.reference );
            }
            else
            {
                ps.setNull( ++count, Types.VARCHAR );
            }
            ps.setInt( ++count, this.locationId );
            ps.setDouble( ++count, this.power );
            ps.setString( ++count, this.type );
            ps.setString( ++count, this.protocol );
            ps.setString( ++count, this.version );
            if( this.lastUpdateDate == null )
            {
                ps.setNull( ++count, java.sql.Types.DATE );
            }
            else
            {
                ps.setDate( ++count, this.lastUpdateDate._getSQLDate());
            }
            if( this.lastUpdateTimeStamp == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, lastUpdateTimeStamp._getSQLTimestamp() );
            }
            ps.setString( ++count, this.machineUniqueRef );
            ps.setInt( ++count, this.userId );
            ps.setInt( ++count, this.chargePointId );

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
        StringBuilder sb = new StringBuilder( "DELETE FROM CHG_POINT WHERE " );
        sb.append( "ID = ? " );

        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setLong( ++count, this.chargePointId );
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
        this.chargePointId = -1;
        this.reference = null;
        this.locationId = -1;
        this.power = -1;
        this.type = null;
        this.protocol = null;
        this.version = null;
        this.lastUpdateDate = null;
        this.machineUniqueRef = null;
        this.lastUpdateTimeStamp = null;
        this.userId = -1;
        this.status = Savable.UNCHANGED;
    }

}
