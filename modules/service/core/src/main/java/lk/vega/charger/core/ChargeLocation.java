package lk.vega.charger.core;

import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.SavingSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dileepa on 3/19/15.
 */
public class ChargeLocation extends Savable
{
    private int locationId;
    private String name;
    private String longitude;
    private String latitude;
    private String gpsLocation;
    private int status;

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public String getGpsLocation()
    {
        return gpsLocation;
    }

    public void setGpsLocation( String gpsLocation )
    {
        this.gpsLocation = gpsLocation;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude( String latitude )
    {
        this.latitude = latitude;
    }

    public int getLocationId()
    {
        return locationId;
    }

    public void setLocationId( int locationId )
    {
        this.locationId = locationId;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude( String longitude )
    {
        this.longitude = longitude;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
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

        StringBuilder sb = new StringBuilder( "INSERT INTO CHG_POINT_LOCATION ( " );
        sb.append( "ID, " );
        sb.append( "LOCATION, " );
        sb.append( "LONGITUDE, " );
        sb.append( "LATTITUDE, " );
        sb.append( "GPSLOCATION, " );
        sb.append( ") VALUES(?,?,?,?,?");
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            if( this.locationId != -1 )
            {
                ps.setInt( ++count, this.locationId );
            }
            else
            {
                ps.setNull( ++count, java.sql.Types.NUMERIC );
            }
            ps.setString( ++count, this.name );
            ps.setString( ++count, this.longitude );
            ps.setString( ++count, this.latitude );
            ps.setString( ++count, this.gpsLocation );

            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }

    private void update( Connection con ) throws SQLException
    {
        StringBuilder sb = new StringBuilder( "UPDATE CHG_POINT_LOCATION SET " );
        sb.append( "LOCATION = ?, " );
        sb.append( "LONGITUDE = ?, " );
        sb.append( "LATTITUDE = ?, " );
        sb.append( "GPSLOCATION = ? " );
        sb.append( "WHERE " );
        sb.append( "ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.name );
            ps.setString( ++count, this.longitude );
            ps.setString( ++count, this.latitude );
            ps.setString( ++count, this.gpsLocation );
            ps.setInt( ++count, this.locationId );

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
        StringBuilder sb = new StringBuilder( "DELETE FROM CHG_POINT_LOCATION WHERE " );
        sb.append( "ID = ? " );

        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setLong( ++count, this.locationId );
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
        this.locationId = -1;
        this.name = null;
        this.longitude = null;
        this.latitude = null;
        this.gpsLocation = null;
        this.status = Savable.UNCHANGED;
    }

    @Override
    public void load( ResultSet rs, Connection con, int level ) throws SQLException
    {
        this.status = Savable.UNCHANGED;
        this.locationId = rs.getInt( "ID" );
        this.name = rs.getString( "LOCATION" );
        this.longitude = rs.getString( "LONGITUDE" );
        this.latitude = rs.getString( "LATTITUDE" );
        this.gpsLocation = rs.getString( "GPSLOCATION" );

    }

}
