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
public class ChargeConnector extends Savable
{
    private int connectorId;
    private int chargePointId;
    private int connectorPortNum;
    private double connectorPower;
    private int status;

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public int getChargePointId()
    {
        return chargePointId;
    }

    public void setChargePointId( int chargePointId )
    {
        this.chargePointId = chargePointId;
    }

    public int getConnectorId()
    {
        return connectorId;
    }

    public void setConnectorId( int connectorId )
    {
        this.connectorId = connectorId;
    }

    public int getConnectorPortNum()
    {
        return connectorPortNum;
    }

    public void setConnectorPortNum( int connectorPortNum )
    {
        this.connectorPortNum = connectorPortNum;
    }

    public double getConnectorPower()
    {
        return connectorPower;
    }

    public void setConnectorPower( double connectorPower )
    {
        this.connectorPower = connectorPower;
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
        this.connectorId = rs.getInt( "CHG_POINT_CONNECTOR_ID" );
        this.chargePointId = rs.getInt( "POINTID" );
        this.connectorPortNum = rs.getInt( "PORTNUM" );
        this.connectorPower = rs.getDouble( "CONNECTORPOWER" );

    }

    public void init()
    {
        this.connectorId = -1;
        this.chargePointId = -1;
        this.connectorPortNum = -1;
        this.connectorPower = -1;
        this.status = Savable.UNCHANGED;
    }

    private void update( Connection con ) throws SQLException
    {

        StringBuilder sb = new StringBuilder( "UPDATE CHG_POINT_CONNECTOR SET " );
        sb.append( "POINTID = ?, " );
        sb.append( "PORTNUM = ?, " );
        sb.append( "CONNECTORPOWER = ? " );
        sb.append( "WHERE " );
        sb.append( "CHG_POINT_CONNECTOR_ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( ++count, this.chargePointId );
            ps.setInt( ++count, this.connectorPortNum );
            ps.setDouble( ++count, this.connectorPower );
            ps.setInt( ++count, this.connectorId );

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
        StringBuilder sb = new StringBuilder( "DELETE FROM CHG_POINT_CONNECTOR WHERE " );
        sb.append( "CHG_POINT_CONNECTOR_ID = ? " );

        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setLong( ++count, this.connectorId );
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
        StringBuilder sb = new StringBuilder( "INSERT INTO CHG_POINT_CONNECTOR ( " );
        sb.append( "POINTID, " );
        sb.append( "PORTNUM, " );
        sb.append( "CONNECTORPOWER " );
        sb.append( ") VALUES(?,?,?)");
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( ++count, this.chargePointId );
            ps.setInt( ++count, this.connectorPortNum );
            ps.setDouble( ++count, this.connectorPower );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }
}
