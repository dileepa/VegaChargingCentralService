package lk.vega.charger.core;

import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.SavingSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/19/15.
 * Time on 2:40 PM
 */
public class ChargeNetworkAndStationMapping extends Savable
{

    private int chgNetworkAndStationMappingId;
    private int networkId;
    private int stationId;
    private int status;

    public int getChgNetworkAndStationMappingId()
    {
        return chgNetworkAndStationMappingId;
    }

    public void setChgNetworkAndStationMappingId( int chgNetworkAndStationMappingId )
    {
        this.chgNetworkAndStationMappingId = chgNetworkAndStationMappingId;
    }

    public int getNetworkId()
    {
        return networkId;
    }

    public void setNetworkId( int networkId )
    {
        this.networkId = networkId;
    }

    public int getStationId()
    {
        return stationId;
    }

    public void setStationId( int stationId )
    {
        this.stationId = stationId;
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

    private void checkValidity()
    {

    }

    @Override
    public void load( ResultSet rs, Connection con, int level ) throws SQLException
    {

        this.status = Savable.UNCHANGED;
        this.chgNetworkAndStationMappingId = rs.getInt( "MAPPING_ID" );
        this.networkId = rs.getInt( "CHG_NETWORK_ID" );
        this.stationId = rs.getInt( "CHG_STATION_ID" );

    }

    public void init()
    {
        this.chgNetworkAndStationMappingId = -1;
        this.networkId = -1;
        this.stationId = -1;
        this.status = Savable.UNCHANGED;
    }


    private void delete( Connection con ) throws SQLException
    {
        StringBuilder sb = new StringBuilder( "DELETE FROM CHG_NETWORK_STATION_MAP WHERE " );
        sb.append( "MAPPING_ID = ? " );

        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( ++count, this.chgNetworkAndStationMappingId );
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

        StringBuilder sb = new StringBuilder( "INSERT INTO CHG_NETWORK_STATION_MAP ( " );
        sb.append( "CHG_NETWORK_ID, " );
        sb.append( "CHG_STATION_ID " );
        sb.append( ") VALUES(?,?)");
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( ++count, this.networkId );
            ps.setInt( ++count, this.stationId );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }

}
