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
 * Date on 5/27/15.
 * Time on 12:22 PM
 */
public class ChargePointTransactionStatus extends Savable
{
    public static final String WORKING_STATUS_BOOKED = "BOOKED";
    public static final String WORKING_STATUS_FREE = "FREE";
    public static final String WORKING_STATUS_IN_PROGRESS = "IN_PROGRESS";


    private int chargePointTransactionId;
    private String chargePointReference;
    private String chargePointWorkingStatus;
    private int status;

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public int getChargePointTransactionId()
    {
        return chargePointTransactionId;
    }

    public void setChargePointTransactionId( int chargePointTransactionId )
    {
        this.chargePointTransactionId = chargePointTransactionId;
    }

    public String getChargePointReference()
    {
        return chargePointReference;
    }

    public void setChargePointReference( String chargePointReference )
    {
        this.chargePointReference = chargePointReference;
    }

    public String getChargePointWorkingStatus()
    {
        return chargePointWorkingStatus;
    }

    public void setChargePointWorkingStatus( String chargePointWorkingStatus )
    {
        this.chargePointWorkingStatus = chargePointWorkingStatus;
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
                insert( con );
            }
            else if( this.status == Savable.MODIFIED )
            {
                action = "Updating";
                update( con );
            }
            else if( this.status == Savable.DELETED )
            {
                action = "Deleting";
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

    protected void insert( Connection con ) throws SQLException
    {
        StringBuilder sb = new StringBuilder( "INSERT INTO CHG_POINT_TRS_STATUS ( " );
        sb.append( "CHG_POINT_REF, " );
        sb.append( "WORKING_STATUS " );
        sb.append( ") VALUES(?,?)" );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.chargePointReference );
            ps.setString( ++count, this.chargePointWorkingStatus );
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
        this.chargePointReference = rs.getString( "CHG_POINT_REF" );
        this.chargePointWorkingStatus = rs.getString( "WORKING_STATUS" );
        this.chargePointTransactionId = rs.getInt( "CHG_POINT_TRS_STATUS_ID" );
    }

    protected void update( Connection con ) throws SQLException
    {
        StringBuilder sb = new StringBuilder( "UPDATE CHG_POINT_TRS_STATUS SET " );
        sb.append( "CHG_POINT_REF = ?, " );
        sb.append( "WORKING_STATUS = ? " );
        sb.append( "WHERE " );
        sb.append( "CHG_POINT_TRS_STATUS_ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.chargePointReference );
            ps.setString( ++count, this.chargePointWorkingStatus );
            ps.setInt( ++count, this.chargePointTransactionId );
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
        StringBuilder sb = new StringBuilder( "DELETE FROM CHG_POINT_TRS_STATUS WHERE " );
        sb.append( "CHG_POINT_TRS_STATUS_ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( ++count, this.chargePointTransactionId );
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
        status = Savable.UNCHANGED;
        chargePointTransactionId = -1;
        chargePointReference = null;
        chargePointWorkingStatus = null;
    }

}
