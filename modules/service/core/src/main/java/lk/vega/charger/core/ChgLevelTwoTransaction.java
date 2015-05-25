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
 * Date on 5/25/15.
 * Time on 1:20 PM
 */
public class ChgLevelTwoTransaction extends Savable
{
    public static final String TRS_STATUS_STARTED = "STARTED";
    public static final String TRS_STATUS_FINISHED = "FINISHED";


    private String trsID;
    private ChgTimeStamp startTime;
    private ChgTimeStamp endTime;
    private String trsStatus;
    private String rfID;
    private String customerUserName;
    private double amount;
    private int networkID;
    private String networkOwner;
    private int chgPointId;
    private String chgPointOwner;
    private String chgPointRef;
    private String networkRef;
    private int status;

    public String getChgPointRef()
    {
        return chgPointRef;
    }

    public void setChgPointRef( String chgPointRef )
    {
        this.chgPointRef = chgPointRef;
    }

    public String getNetworkRef()
    {
        return networkRef;
    }

    public void setNetworkRef( String networkRef )
    {
        this.networkRef = networkRef;
    }

    @Override
    public int getStatus()
    {
        return status;
    }

    @Override
    public void setStatus( int status )
    {
        this.status = status;
    }

    public String getTrsID()
    {
        return trsID;
    }

    public void setTrsID( String trsID )
    {
        this.trsID = trsID;
    }

    public ChgTimeStamp getStartTime()
    {
        return startTime;
    }

    public void setStartTime( ChgTimeStamp startTime )
    {
        this.startTime = startTime;
    }

    public ChgTimeStamp getEndTime()
    {
        return endTime;
    }

    public void setEndTime( ChgTimeStamp endTime )
    {
        this.endTime = endTime;
    }

    public String getTrsStatus()
    {
        return trsStatus;
    }

    public void setTrsStatus( String trsStatus )
    {
        this.trsStatus = trsStatus;
    }

    public String getRfID()
    {
        return rfID;
    }

    public void setRfID( String rfID )
    {
        this.rfID = rfID;
    }

    public String getCustomerUserName()
    {
        return customerUserName;
    }

    public void setCustomerUserName( String customerUserName )
    {
        this.customerUserName = customerUserName;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount( double amount )
    {
        this.amount = amount;
    }

    public int getNetworkID()
    {
        return networkID;
    }

    public void setNetworkID( int networkID )
    {
        this.networkID = networkID;
    }

    public String getNetworkOwner()
    {
        return networkOwner;
    }

    public void setNetworkOwner( String networkOwner )
    {
        this.networkOwner = networkOwner;
    }

    public int getChgPointId()
    {
        return chgPointId;
    }

    public void setChgPointId( int chgPointId )
    {
        this.chgPointId = chgPointId;
    }

    public String getChgPointOwner()
    {
        return chgPointOwner;
    }

    public void setChgPointOwner( String chgPointOwner )
    {
        this.chgPointOwner = chgPointOwner;
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

    private void insert( Connection con ) throws SQLException
    {

        this.startTime = new ChgTimeStamp(  );
        this.endTime = new ChgTimeStamp(  );

        StringBuilder sb = new StringBuilder( "INSERT INTO TRS_LEVEL_TWO_CHARGER ( " );
        sb.append( "TRS_ID, " );
        sb.append( "START_TIME, " );
        sb.append( "END_TIME, " );
        sb.append( "TRS__STATUS, " );
        sb.append( "REF_ID, " );
        sb.append( "CUSTOMER_USERNAME, " );
        sb.append( "CHG_AMOUNT, " );
        sb.append( "NETWORK_ID, " );
        sb.append( "CHG_POINT_ID, " );
        sb.append( "CHG_POINT_OWNER, " );
        sb.append( "CHG_POINT_REF, " );
        sb.append( "NETWORK_REF, " );
        sb.append( "NETWORK_OWNER " );
        sb.append( ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)" );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.trsID );
            if( this.startTime == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, startTime._getSQLTimestamp());
            }
            if( this.endTime == null )
            {
                ps.setNull( ++count, Types.TIMESTAMP );
            }
            else
            {
                ps.setTimestamp( ++count, endTime._getSQLTimestamp() );
            }
            ps.setString( ++count, this.trsStatus );
            ps.setString( ++count, this.rfID );
            ps.setString( ++count, this.customerUserName );
            ps.setDouble( ++count, this.amount );
            ps.setInt( ++count, this.networkID );
            ps.setInt( ++count, this.chgPointId );
            ps.setString( ++count, this.chgPointOwner );
            ps.setString( ++count, this.chgPointRef );
            ps.setString( ++count, this.networkRef );
            ps.setString( ++count, this.networkOwner );
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
        this.endTime = new ChgTimeStamp(  );

        StringBuilder sb = new StringBuilder( "UPDATE TRS_LEVEL_TWO_CHARGER SET " );
        sb.append( "START_TIME = ?, " );
        sb.append( "END_TIME = ?, " );
        sb.append( "TRS__STATUS = ?, " );
        sb.append( "REF_ID = ?, " );
        sb.append( "CUSTOMER_USERNAME = ?, " );
        sb.append( "CHG_AMOUNT = ?, " );
        sb.append( "NETWORK_ID = ?, " );
        sb.append( "CHG_POINT_ID = ?, " );
        sb.append( "CHG_POINT_OWNER = ?, " );
        sb.append( "CHG_POINT_REF = ?, " );
        sb.append( "NETWORK_REF = ?, " );
        sb.append( "NETWORK_OWNER = ? " );
        sb.append( "WHERE " );
        sb.append( "TRS_ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
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
            ps.setString( ++count, this.trsStatus );
            ps.setString( ++count, this.rfID );
            ps.setString( ++count, this.customerUserName );
            ps.setDouble( ++count, this.amount );
            ps.setInt( ++count, this.networkID );
            ps.setInt( ++count, this.chgPointId );
            ps.setString( ++count, this.chgPointOwner );
            ps.setString( ++count, this.chgPointRef );
            ps.setString( ++count, this.networkRef );
            ps.setString( ++count, this.networkOwner );
            ps.setString( ++count, this.trsID );
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
        StringBuilder sb = new StringBuilder( "DELETE FROM TRS_LEVEL_TWO_CHARGER WHERE " );
        sb.append( "TRS_ID = ? " );

        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.trsID );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }

    /**
     *
     * @param chgTimeStamp
     * @param chargeRef
     * PhoneNum - Integer Value of phone Number
     * yearVal - ex : year is 2015 last 2 digits of year(15*pow(10,7) -> 150000000)
     * dayVal - ex : mar 22 -> 84 (day num of year max 366) -> 84*pow(10,4) -> 840000
     * time val - ex : time 03:45 -> 0345
     * final unique key- yearVal+ dayVal+timeVal
     * 150000000
     *    840000
     *      0345
     *final -> 150840345
     * @return transactionID = uniqueDateKey
     */
        public static String generateTransactionID(ChgTimeStamp chgTimeStamp, String chargeRef)
        {
            int modifyYearValue = chgTimeStamp.getLastTwoDigitsOfYear() * (int)Math.pow( 10,7 );
            int modifyDayValue =  chgTimeStamp.getDayOfYear() * (int)Math.pow( 10,4 );
            int modifyTimeValue = chgTimeStamp._getTimeValue();
            int uniqueKeyFromDate = modifyYearValue + modifyDayValue + modifyTimeValue;
            StringBuilder sb = new StringBuilder(  );
            sb.append( uniqueKeyFromDate );
            sb.append( "_" );
            sb.append( chargeRef );
            return  sb.toString();
        }


    @Override
    public void load( ResultSet rs, Connection con, int level ) throws SQLException
    {
        this.status = Savable.UNCHANGED;
        this.trsID = rs.getString( "TRS_ID" );
        this.startTime = new ChgTimeStamp( rs.getTimestamp( "START_TIME" ) );
        this.endTime = new ChgTimeStamp( rs.getTimestamp( "END_TIME" ) );
        this.trsStatus = rs.getString( "TRS__STATUS" );
        this.rfID = rs.getString( "REF_ID" );
        this.customerUserName = rs.getString( "CUSTOMER_USERNAME" );
        this.chgPointRef = rs.getString( "CHG_POINT_REF" );
        this.networkRef = rs.getString( "NETWORK_REF" );
        this.amount = rs.getDouble( "CHG_AMOUNT" );
        this.networkID = rs.getInt( "NETWORK_ID" );
        this.chgPointId = rs.getInt( "CHG_POINT_ID" );
        this.chgPointOwner = rs.getString( "CHG_POINT_OWNER" );
        this.networkOwner = rs.getString( "NETWORK_OWNER" );
    }

    public void init()
    {
        this.trsID = null;
        this.startTime = null;
        this.endTime = null;
        this.trsStatus = null;
        this.rfID = null;
        this.customerUserName = null;
        this.chgPointRef = null;
        this.networkRef = null;
        this.amount = 0.0d;
        this.networkID = -1;
        this.chgPointId = -1;
        this.chgPointOwner = null;
        this.networkOwner = null;
        this.status = Savable.UNCHANGED;
    }

}
