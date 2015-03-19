package lk.vega.charger.core;

/**
 * Created by dileepa on 3/19/15.
 */

import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.SavingSQLException;
import lk.vega.charger.util.VegaTimestamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ChargeTransaction extends Savable
{
    private int id;
    private String transactionId;
    private VegaTimestamp startTime;
    private VegaTimestamp endTime;
    private String authenticationKey;
    private int pointId;
    private double initialAmount;
    private double finalAmount;
    private double energyConsumption;
    private String crossReference;
    private int status;
    private int _meterStart;
    private int _meterEnd;

    public String getTransactionStatus()
    {
        return transactionStatus;
    }

    public void setTransactionStatus( String transactionStatus )
    {
        this.transactionStatus = transactionStatus;
    }

    private String transactionStatus;

    public int getReservationId()
    {
        return reservationId;
    }

    public void setReservationId( int reservationId )
    {
        this.reservationId = reservationId;
    }

    private int reservationId;

    public int _getMeterStart()
    {
        return _meterStart;
    }

    public void _setMeterStart( int _meterStart )
    {
        this._meterStart = _meterStart;
    }

    public int _getMeterEnd()
    {
        return _meterEnd;
    }

    public void _setMeterEnd( int _meterEnd )
    {
        this._meterEnd = _meterEnd;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }

    public String getAuthenticationKey()
    {
        return authenticationKey;
    }

    public void setAuthenticationKey( String authenticationKey )
    {
        this.authenticationKey = authenticationKey;
    }

    public String getCrossReference()
    {
        return crossReference;
    }

    public void setCrossReference( String crossReference )
    {
        this.crossReference = crossReference;
    }

    public VegaTimestamp getEndTime()
    {
        return endTime;
    }

    public void setEndTime( VegaTimestamp endTime )
    {
        this.endTime = endTime;
    }

    public double getEnergyConsumption()
    {
        return energyConsumption;
    }

    public void setEnergyConsumption( double energyConsumption )
    {
        this.energyConsumption = energyConsumption;
    }

    public double getFinalAmount()
    {
        return finalAmount;
    }

    public void setFinalAmount( double finalAmount )
    {
        this.finalAmount = finalAmount;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public double getInitialAmount()
    {
        return initialAmount;
    }

    public void setInitialAmount( double initialAmount )
    {
        this.initialAmount = initialAmount;
    }

    public int getPointId()
    {
        return pointId;
    }

    public void setPointId( int pointId )
    {
        this.pointId = pointId;
    }

    public VegaTimestamp getStartTime()
    {
        return startTime;
    }

    public void setStartTime( VegaTimestamp startTime )
    {
        this.startTime = startTime;
    }

    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId( String transactionId )
    {
        this.transactionId = transactionId;
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
        StringBuilder sb = new StringBuilder( "INSERT INTO TRS_CHG_TRANSACTION ( " );
        sb.append( "ID, " );
        sb.append( "TRS_ID, " );
        sb.append( "STATR_TIME, " );
        sb.append( "END_TIME, " );
        sb.append( "AUTENTICATION_KEY, " );
        sb.append( "POINTID, " );
        sb.append( "INITIAL_AMOUNT, " );
        sb.append( "FINAL_AMOUNT, " );
        sb.append( "ENERGY_CONSUMPTION, " );
        sb.append( "CROSS_REFERENCE " );
        sb.append( ") VALUES(?,?,?,?,?,?,?,?,?,?");
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

            if( this.transactionId != null )
            {
                ps.setString( ++count, this.transactionId );
            }
            else
            {
                ps.setNull( ++count, Types.VARCHAR );
            }
            if( this.startTime == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, startTime._getSQLTimestamp() );
            }
            if( this.endTime == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, endTime._getSQLTimestamp() );
            }
            ps.setString( ++count, this.authenticationKey );
            ps.setInt( ++count, this.pointId );
            ps.setDouble( ++count, this.initialAmount );
            ps.setDouble( ++count, this.finalAmount );
            ps.setDouble( ++count, this.energyConsumption );
            ps.setString( ++count, this.crossReference );
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
        this.id = rs.getInt( "ID" );
        this.transactionId = rs.getString( "TRS_ID" );
        this.startTime = new VegaTimestamp( rs.getTimestamp( "STATR_TIME" ));
        this.endTime = new VegaTimestamp(rs.getTimestamp( "END_TIME" ));
        this.authenticationKey = rs.getString( "AUTENTICATION_KEY" );
        this.pointId = rs.getInt( "POINTID" );
        this.initialAmount = rs.getDouble( "INITIAL_AMOUNT" );
        this.finalAmount = rs.getDouble( "FINAL_AMOUNT" );
        this.energyConsumption = rs.getDouble( "ENERGY_CONSUMPTION" );
        this.crossReference = rs.getString( "CROSS_REFERENCE" );
    }

    private void update( Connection con ) throws SQLException
    {

        StringBuilder sb = new StringBuilder( "UPDATE TRS_CHG_TRANSACTION SET " );
        sb.append( "TRS_ID = ?, " );
        sb.append( "STATR_TIME = ?, " );
        sb.append( "END_TIME = ?, " );
        sb.append( "AUTENTICATION_KEY = ?, " );
        sb.append( "POINTID = ?, " );
        sb.append( "INITIAL_AMOUNT = ?, " );
        sb.append( "FINAL_AMOUNT = ?, " );
        sb.append( "ENERGY_CONSUMPTION = ?, " );
        sb.append( "CROSS_REFERENCE = ? " );
        sb.append( "WHERE " );
        sb.append( "ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            if( this.transactionId != null )
            {
                ps.setString( ++count, this.transactionId );
            }
            else
            {
                ps.setNull( ++count, Types.VARCHAR );
            }
            if( this.startTime == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, startTime._getSQLTimestamp() );
            }
            if( this.endTime == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, endTime._getSQLTimestamp() );
            }
            ps.setString( ++count, this.authenticationKey );
            ps.setInt( ++count, this.pointId );
            ps.setDouble( ++count, this.initialAmount );
            ps.setDouble( ++count, this.finalAmount );
            ps.setDouble( ++count, this.energyConsumption );
            ps.setString( ++count, this.crossReference );
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
        StringBuilder sb = new StringBuilder( "DELETE FROM TRS_CHG_TRANSACTION WHERE " );
        sb.append( "ID = ? " );

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
        this.transactionId = null;
        this.startTime = null;
        this.endTime = null;
        this.authenticationKey = null;
        this.pointId = -1;
        this.initialAmount = -1;
        this.finalAmount = -1;
        this.energyConsumption = -1;
        this.crossReference = null;
        this._meterEnd = -1;
        this._meterStart = -1;
        this.reservationId = -1;
        this.transactionStatus = null;
        this.status = Savable.UNCHANGED;
    }


}
