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
    private double maxChargeTime;
    private double chargeAmount;
    private String type;
    private String protocol;
    private String firmwareVersion;
    private String hardwareVersion;
    private ChgDate lastUpdateDate;
    private ChgTimeStamp lastUpdateTimeStamp;
    private String userName;
    private String machineUniqueRef;
    private String chargePointAvailabilityStatus;
    private String chargePointPowerStatus;
    private String adminUserName;
    private int status;

    public double getChargeAmount()
    {
        return chargeAmount;
    }

    public void setChargeAmount( double chargeAmount )
    {
        this.chargeAmount = chargeAmount;
    }

    public double getMaxChargeTime()
    {
        return maxChargeTime;
    }

    public void setMaxChargeTime( double maxChargeTime )
    {
        this.maxChargeTime = maxChargeTime;
    }

    public String getAdminUserName()
    {
        return adminUserName;
    }

    public void setAdminUserName( String adminUserName )
    {
        this.adminUserName = adminUserName;
    }

    public String getMachineUniqueRef()
    {
        return machineUniqueRef;
    }

    public void setMachineUniqueRef( String machineUniqueRef )
    {
        this.machineUniqueRef = machineUniqueRef;
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

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public String getFirmwareVersion()
    {
        return firmwareVersion;
    }

    public void setFirmwareVersion( String firmwareVersion )
    {
        this.firmwareVersion = firmwareVersion;
    }

    public String getHardwareVersion()
    {
        return hardwareVersion;
    }

    public void setHardwareVersion( String hardwareVersion )
    {
        this.hardwareVersion = hardwareVersion;
    }

    public String getChargePointAvailabilityStatus()
    {
        return chargePointAvailabilityStatus;
    }

    public void setChargePointAvailabilityStatus( String chargePointAvailabilityStatus )
    {
        this.chargePointAvailabilityStatus = chargePointAvailabilityStatus;
    }

    public String getChargePointPowerStatus()
    {
        return chargePointPowerStatus;
    }

    public void setChargePointPowerStatus( String chargePointPowerStatus )
    {
        this.chargePointPowerStatus = chargePointPowerStatus;
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

        this.lastUpdateDate = new ChgDate( );
        this.lastUpdateTimeStamp = new ChgTimeStamp( );

        StringBuilder sb = new StringBuilder( "INSERT INTO CHG_POINT ( " );
        sb.append( "REFERENCE, " );
        sb.append( "LOCATIONID, " );
        sb.append( "POWER, " );
        sb.append( "MAX_CHARGE_TIME, " );
        sb.append( "CHG_POINT_AMOUNT, " );
        sb.append( "TYPE, " );
        sb.append( "PROTOCOL, " );
        sb.append( "FIRMWARE_VERSION, " );
        sb.append( "HARDWARE_VERSION, " );
        sb.append( "LASTUPDATE, " );
        sb.append( "LASTUPDATETIMESTAMP, " );
        sb.append( "MACHINE_UNIQUE_ADDRESS, " );
        sb.append( "AVAILABILITY_STATUS, " );
        sb.append( "POWER_STATUS, " );
        sb.append( "ADMIN_USERNAME, " );
        sb.append( "OWNER_USERNAME " );
        sb.append( ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
            ps.setDouble( ++count, this.maxChargeTime );
            ps.setDouble( ++count, this.chargeAmount );
            ps.setString( ++count, this.type );
            ps.setString( ++count, this.protocol );
            ps.setString( ++count, this.firmwareVersion );
            ps.setString( ++count, this.hardwareVersion );
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
            ps.setString( ++count, this.chargePointAvailabilityStatus );
            ps.setString( ++count, this.chargePointPowerStatus );
            ps.setString( ++count, this.adminUserName );
            ps.setString( ++count, this.userName );

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
        this.chargePointId = rs.getInt( "CHG_POINT_ID" );
        this.reference = rs.getString( "REFERENCE" );
        this.locationId = rs.getInt( "LOCATIONID" );
        this.power = rs.getDouble( "POWER" );
        this.maxChargeTime = rs.getDouble( "MAX_CHARGE_TIME" );
        this.chargeAmount = rs.getDouble( "CHG_POINT_AMOUNT" );
        this.type = rs.getString( "TYPE" );
        this.protocol = rs.getString( "PROTOCOL" );
        this.firmwareVersion = rs.getString( "FIRMWARE_VERSION" );
        this.hardwareVersion = rs.getString( "HARDWARE_VERSION" );
        this.lastUpdateDate = new ChgDate( rs.getDate( "LASTUPDATE" ) );
        this.machineUniqueRef = rs.getString( "MACHINE_UNIQUE_ADDRESS" );
        this.lastUpdateTimeStamp = new ChgTimeStamp( rs.getTimestamp( "LASTUPDATETIMESTAMP" ) );
        this.userName = rs.getString( "OWNER_USERNAME" );
        this.adminUserName = rs.getString( "ADMIN_USERNAME" );
        this.chargePointAvailabilityStatus = rs.getString( "AVAILABILITY_STATUS" );
        this.chargePointPowerStatus = rs.getString( "POWER_STATUS" );
//        if( level > 10 )
//        {
//            PreparedStatement psChargeLocation = null;
//            ResultSet rsChargeLocation = null;
//
//            try
//            {
//                StringBuilder chargeLocationString = new StringBuilder( "SELECT * FROM CHG_POINT_LOCATION WHERE " );
//                chargeLocationString.append( "ID = ? " );
//
//                psChargeLocation = con.prepareStatement( chargeLocationString.toString() );
//                psChargeLocation.setInt( 1, locationId );
//                rsChargeLocation = psChargeLocation.executeQuery();
//                if( rsChargeLocation.next() )
//                {
//                    ChargeLocation chargeLocation = new ChargeLocation();
//                    chargeLocation.init();
//                    chargeLocation.load( rsChargeLocation, con, 0 );
//                    this.chargeLocation = chargeLocation;
//                }
//            }
//            finally
//            {
//                DBUtility.close( rsChargeLocation );
//                DBUtility.close( psChargeLocation );
//            }
//
//        }
    }

    private void update( Connection con ) throws SQLException
    {
        this.lastUpdateDate = new ChgDate( );
        this.lastUpdateTimeStamp = new ChgTimeStamp( );

        StringBuilder sb = new StringBuilder( "UPDATE CHG_POINT SET " );
        sb.append( "REFERENCE = ?, " );
        sb.append( "LOCATIONID = ?, " );
        sb.append( "POWER = ?, " );
        sb.append( "MAX_CHARGE_TIME = ?, " );
        sb.append( "CHG_POINT_AMOUNT = ?, " );
        sb.append( "TYPE = ?, " );
        sb.append( "PROTOCOL = ?, " );
        sb.append( "FIRMWARE_VERSION = ?, " );
        sb.append( "HARDWARE_VERSION = ?, " );
        sb.append( "LASTUPDATE = ?, " );
        sb.append( "LASTUPDATETIMESTAMP = ?, " );
        sb.append( "MACHINE_UNIQUE_ADDRESS = ?, " );
        sb.append( "OWNER_USERNAME = ?, " );
        sb.append( "ADMIN_USERNAME = ?, " );
        sb.append( "AVAILABILITY_STATUS = ?, " );
        sb.append( "POWER_STATUS = ? " );
        sb.append( "WHERE " );
        sb.append( "CHG_POINT_ID = ? " );
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
            ps.setDouble( ++count, this.maxChargeTime );
            ps.setDouble( ++count, this.chargeAmount );
            ps.setString( ++count, this.type );
            ps.setString( ++count, this.protocol );
            ps.setString( ++count, this.firmwareVersion );
            ps.setString( ++count, this.hardwareVersion );
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
            ps.setString( ++count, this.userName );
            ps.setString( ++count, this.adminUserName );
            ps.setString( ++count, this.chargePointAvailabilityStatus );
            ps.setString( ++count, this.chargePointPowerStatus );
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
        sb.append( "CHG_POINT_ID = ? " );

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
        this.power = 0.0d;
        this.maxChargeTime = 0.0d;
        this.chargeAmount = 0.0d;
        this.type = null;
        this.protocol = null;
        this.firmwareVersion = null;
        this.hardwareVersion = null;
        this.lastUpdateDate = null;
        this.machineUniqueRef = null;
        this.lastUpdateTimeStamp = null;
        this.chargePointAvailabilityStatus = null;
        this.chargePointPowerStatus = null;
        this.userName = null;
        this.adminUserName = null;
        this.status = Savable.UNCHANGED;
    }

}
