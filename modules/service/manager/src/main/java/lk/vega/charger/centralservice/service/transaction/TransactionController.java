package lk.vega.charger.centralservice.service.transaction;

import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.ChgTimeStamp;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.connection.CHGConnectionPoolFactory;
import ocpp.cs._2012._06.StartTransactionRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dileepa on 3/19/15.
 */
public class TransactionController
{
    public static final String TRS_STARTED = "STARTED";
    public static final String TRS_PROCESSED = "PROCESSED";
    public static final String TRS_FINISHED = "FINISHED";
    public static final String TRS_NEW= "NEW_ONE";
    public static final String TRS_CROSS_REF_SPLITTER= "%";
    public static final String TRS_AUTH_KEY_SPLITTER = "#";

    /**
     * id tag format - phonenum#intialamount#timestamp%crossReference
     * authenticationKey format - phonenum#intialamount#timestamp
     * @param parameters
     * @return
     */
    public static ChargeTransaction generateTransaction( StartTransactionRequest parameters )
    {
        String transactionId = parameters.getIdTag(); //TODO can override it, temp solution - format -authKey%CrossRef

        String []authKeyCroRefArray = phoneNumAmountAndCrossRefSeparator( parameters.getIdTag() );
        String authenticationKey = authKeyCroRefArray[0];
        String crossRef = authKeyCroRefArray[1];
        String phoneAmountTimeArray[] = phoneNumAmountAndDateSeparator( authenticationKey );
        String phoneNum = phoneAmountTimeArray[0];
        String initialAmount = phoneAmountTimeArray[1];

        ChargeTransaction chargeTransaction = new ChargeTransaction();
        chargeTransaction.init();
        chargeTransaction.setTransactionId( transactionId );
        ChgTimeStamp startingTime = new ChgTimeStamp(  );
        chargeTransaction.setStartTime( startingTime );
        chargeTransaction.setEndTime( startingTime );
        chargeTransaction.setAuthenticationKey( authenticationKey );
        chargeTransaction.setPointId( parameters.getConnectorId() );
        chargeTransaction.setInitialAmount( Double.parseDouble( initialAmount ) );
        chargeTransaction.setFinalAmount( 0.0d );
        chargeTransaction.setEnergyConsumption( 0.0d );
        chargeTransaction.setCrossReference( crossRef );
        chargeTransaction.setMeterStart( parameters.getMeterStart() );
        chargeTransaction.setMeterEnd( 0 );
        chargeTransaction.setTransactionStatus( TRS_STARTED );
        chargeTransaction.setReservationId( parameters.getReservationId() );
        chargeTransaction.setStatus( Savable.NEW );
        return chargeTransaction;

    }

    public static ChgResponse loadProcessingTransaction( String authorizeKey, String state )
    {
        //TODO db loading part.
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM TRS_CHG_TRANSACTION WHERE AUTENTICATION_KEY = ? AND STATUS =? " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString());
            ps.setString( 1,authorizeKey );
            ps.setString( 2, state );
            rs = ps.executeQuery();
            if (rs.next())
            {
                ChargeTransaction chargeTransaction = new ChargeTransaction();
                chargeTransaction.init();
                chargeTransaction.load( rs,con,0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Transaction Successfully", chargeTransaction );
            }

        }
        catch( SQLException e )
        {
            e.printStackTrace();
            return new ChgResponse( ChgResponse.ERROR, e.getMessage() );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            return new ChgResponse( ChgResponse.ERROR, e.getMessage());
        }
        finally
        {
            DBUtility.close( rs );
            DBUtility.close( ps );
            DBUtility.close( con );
        }
        StringBuilder s = new StringBuilder(  );
        s.append( "Could not find Inprogress Transaction for Authentication Key : " );
        s.append( authorizeKey );
        s.append( "." );
        return new ChgResponse( ChgResponse.SUCCESS, s.toString(), null, TRS_NEW );
    }

    /**
     *
     * @param key format - phonenum#intialamount#timestamp%crossReference
     * @return [0] - phonenum#intialamount#timestamp
     *         [1] - crossReference
     */
    public static String[] phoneNumAmountAndCrossRefSeparator( String key )
    {
        return key.split( TRS_CROSS_REF_SPLITTER );
    }

    /**
     *
     * @param key format - phonenum#intialamount#timestamp
     * @return [0] - phonenum
     *         [1] - intialamount
     *         [2] - timestamp
     */
    public static String[] phoneNumAmountAndDateSeparator( String key )
    {
        return key.split( TRS_AUTH_KEY_SPLITTER );
    }


}
