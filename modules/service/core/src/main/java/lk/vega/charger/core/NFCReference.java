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
 * Date on 5/21/15.
 * Time on 3:22 PM
 */
public class NFCReference extends Savable
{
    private int nfcNetworkMapID;
    private String reference;
    private int networkId;
    private int status;

    public static final String DUMMY_NFC_REF = "DUMMY_NFC_REF";

    public int getNfcNetworkMapID()
    {
        return nfcNetworkMapID;
    }

    public void setNfcNetworkMapID( int nfcNetworkMapID )
    {
        this.nfcNetworkMapID = nfcNetworkMapID;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference( String reference )
    {
        this.reference = reference;
    }

    public int getNetworkId()
    {
        return networkId;
    }

    public void setNetworkId( int networkId )
    {
        this.networkId = networkId;
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
        StringBuilder sb = new StringBuilder( "INSERT INTO NFC_NETWORK_MAP ( " );
        sb.append( "NFC_ID, " );
        sb.append( "CHG_NETWORK_ID " );
        sb.append( ") VALUES(?,?)" );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.reference );
            ps.setInt( ++count, this.networkId );
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

    }

    protected void delete( Connection con ) throws SQLException
    {
        StringBuilder sb = new StringBuilder( "DELETE FROM NFC_NETWORK_MAP WHERE " );
        sb.append( "NFC_NETWORK_MAP_ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( ++count, this.nfcNetworkMapID );
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
        this.nfcNetworkMapID = rs.getInt( "NFC_NETWORK_MAP_ID" );
        this.reference = rs.getString( "NFC_ID" );
        this.networkId = rs.getInt( "CHG_NETWORK_ID" );
    }

    public void init()
    {
        reference = null;
        nfcNetworkMapID = -1;
        networkId = -1;
        status = Savable.UNCHANGED;
    }
}
