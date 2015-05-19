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
 * Date on 5/15/15.
 * Time on 2:37 PM
 */
public class ChargeNetwork extends Savable
{

    private int networkId;
    private String reference;
    private String networkOwnerUserName;
    private double chargeAmount;
    private double chargeAmountForOtherNetwork;
    private double membershipFee;
    private double annualFee;
    private double maxChargeTime;
    private ChgTimeStamp lastUpdateTimeStamp;
    private ChgTimeStamp createdTimeStamp;
    private int status;


    public int getNetworkId()
    {
        return networkId;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public void setNetworkId( int networkId )
    {
        this.networkId = networkId;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference( String reference )
    {
        this.reference = reference;
    }

    public String getNetworkOwnerUserName()
    {
        return networkOwnerUserName;
    }

    public void setNetworkOwnerUserName( String networkOwnerUserName )
    {
        this.networkOwnerUserName = networkOwnerUserName;
    }

    public double getChargeAmount()
    {
        return chargeAmount;
    }

    public void setChargeAmount( double chargeAmount )
    {
        this.chargeAmount = chargeAmount;
    }

    public double getChargeAmountForOtherNetwork()
    {
        return chargeAmountForOtherNetwork;
    }

    public void setChargeAmountForOtherNetwork( double chargeAmountForOtherNetwork )
    {
        this.chargeAmountForOtherNetwork = chargeAmountForOtherNetwork;
    }

    public double getMembershipFee()
    {
        return membershipFee;
    }

    public void setMembershipFee( double membershipFee )
    {
        this.membershipFee = membershipFee;
    }

    public double getAnnualFee()
    {
        return annualFee;
    }

    public void setAnnualFee( double annualFee )
    {
        this.annualFee = annualFee;
    }

    public double getMaxChargeTime()
    {
        return maxChargeTime;
    }

    public void setMaxChargeTime( double maxChargeTime )
    {
        this.maxChargeTime = maxChargeTime;
    }

    public ChgTimeStamp getLastUpdateTimeStamp()
    {
        return lastUpdateTimeStamp;
    }


    public ChgTimeStamp getCreatedTimeStamp()
    {
        return createdTimeStamp;
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

        this.createdTimeStamp = new ChgTimeStamp( );
        this.lastUpdateTimeStamp = new ChgTimeStamp( );

        StringBuilder sb = new StringBuilder( "INSERT INTO CHG_NETWORK ( " );
        sb.append( "NETWORK_REFERENCE, " );
        sb.append( "OWNER_USER_NAME, " );
        sb.append( "CHARGE_AMOUNT, " );
        sb.append( "CHARGE_AMOUNT_OUT_OF_NETWORK, " );
        sb.append( "MAX_CHG_TIME, " );
        sb.append( "MEMBERSHIP_FEE, " );
        sb.append( "ANNUAL_FEE, " );
        sb.append( "CREATED_TIME, " );
        sb.append( "LAST_UPDATED_TIME " );
        sb.append( ") VALUES(?,?,?,?,?,?,?,?,?)");
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
            ps.setString( ++count, this.networkOwnerUserName );
            ps.setDouble( ++count, this.chargeAmount );
            ps.setDouble( ++count, this.chargeAmountForOtherNetwork );
            ps.setDouble( ++count, this.maxChargeTime );
            ps.setDouble( ++count, this.membershipFee );
            ps.setDouble( ++count, this.annualFee );
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
                ps.setTimestamp( ++count, lastUpdateTimeStamp._getSQLTimestamp());
            }

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
        this.networkId = rs.getInt( "NETWORK_ID" );
        this.reference = rs.getString( "NETWORK_REFERENCE" );
        this.networkOwnerUserName = rs.getString( "OWNER_USER_NAME" );
        this.chargeAmount = rs.getDouble( "CHARGE_AMOUNT" );
        this.chargeAmountForOtherNetwork = rs.getDouble( "CHARGE_AMOUNT_OUT_OF_NETWORK" );
        this.maxChargeTime = rs.getDouble( "MAX_CHG_TIME" );
        this.membershipFee = rs.getDouble( "MEMBERSHIP_FEE" );
        this.annualFee = rs.getDouble( "ANNUAL_FEE" );
        this.createdTimeStamp = new ChgTimeStamp( rs.getTimestamp( "CREATED_TIME" ) );
        this.lastUpdateTimeStamp = new ChgTimeStamp( rs.getTimestamp( "LAST_UPDATED_TIME" ) );
    }

    public void init()
    {
        this.networkId = -1;
        this.reference = null;
        this.networkOwnerUserName = null;
        this.chargeAmount = 0.0d;
        this.chargeAmountForOtherNetwork = 0.0d;
        this.maxChargeTime = 0.0d;
        this.membershipFee = 0.0d;
        this.annualFee = 0.0d;
        this.createdTimeStamp = null;
        this.lastUpdateTimeStamp = null;
        this.status = Savable.UNCHANGED;
    }

    private void update( Connection con ) throws SQLException
    {
        this.lastUpdateTimeStamp = new ChgTimeStamp( );

        StringBuilder sb = new StringBuilder( "UPDATE CHG_NETWORK SET " );
        sb.append( "NETWORK_REFERENCE = ?, " );
        sb.append( "OWNER_USER_NAME = ?, " );
        sb.append( "CHARGE_AMOUNT = ?, " );
        sb.append( "CHARGE_AMOUNT_OUT_OF_NETWORK = ?, " );
        sb.append( "MAX_CHG_TIME = ?, " );
        sb.append( "MEMBERSHIP_FEE = ?, " );
        sb.append( "ANNUAL_FEE = ?, " );
        sb.append( "CREATED_TIME = ?, " );
        sb.append( "LAST_UPDATED_TIME = ? " );
        sb.append( "WHERE " );
        sb.append( "NETWORK_ID = ? " );
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
            ps.setString( ++count, this.networkOwnerUserName );
            ps.setDouble( ++count, this.chargeAmount );
            ps.setDouble( ++count, this.chargeAmountForOtherNetwork );
            ps.setDouble( ++count, this.maxChargeTime );
            ps.setDouble( ++count, this.membershipFee );
            ps.setDouble( ++count, this.annualFee );
            if( this.createdTimeStamp == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, createdTimeStamp._getSQLTimestamp() );
            }
            if( this.lastUpdateTimeStamp == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, lastUpdateTimeStamp._getSQLTimestamp() );
            }
            ps.setInt( ++count, this.networkId );

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
        StringBuilder sb = new StringBuilder( "DELETE FROM CHG_NETWORK WHERE " );
        sb.append( "NETWORK_ID = ? " );

        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setLong( ++count, this.networkId );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }


}
