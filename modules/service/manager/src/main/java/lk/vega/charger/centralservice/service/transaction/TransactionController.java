package lk.vega.charger.centralservice.service.transaction;

import lk.vega.charger.centralservice.service.paymentgateway.PaymentDetail;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentGateWay;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentGateWayFactory;
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
    private static int TRS_ID_COUNTER_PER_DAY = 0;
    private static int MAXIMUM_TRS_ID_COUNTER = 9999;

    /**
     * id tag format - phonenum#intialamount#timestamp%crossReference
     * authenticationKey format - phonenum#intialamount#timestamp
     * @param parameters
     * @return
     */
    public static ChargeTransaction generateTransaction( StartTransactionRequest parameters )
    {

        String []authKeyCroRefArray = phoneNumAmountAndCrossRefSeparator( parameters.getIdTag() );
        String authenticationKey = authKeyCroRefArray[0];
        String crossRef = authKeyCroRefArray[1];
        String phoneAmountTimeArray[] = phoneNumAmountAndDateSeparator( authenticationKey );
        String phoneNum = phoneAmountTimeArray[0];
        String initialAmount = phoneAmountTimeArray[1];
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setAuthenticationKey( authenticationKey );
        PaymentGateWay paymentGateWay = PaymentGateWayFactory.selectPaymentGateWay( paymentDetail );

//        int transactionId = generateTransactionID( new ChgTimeStamp(  ), phoneNum );
        int transactionId = generateTransactionID( new ChgTimeStamp(  ) );

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
        chargeTransaction.setPaymentGateWayType( paymentGateWay.getPaymentGateWayType() );
        chargeTransaction.setStatus( Savable.NEW );
        return chargeTransaction;

    }

    public static ChgResponse loadProcessingTransaction( String authorizeKey, String state, String transactionId )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM TRS_CHG_TRANSACTION WHERE STATUS =? " );
        boolean transactionIdNotExist = "".equals( transactionId );
        sb = transactionIdNotExist ? sb.append( "AND AUTENTICATION_KEY = ?" ) :sb.append( "AND TRS_ID = ? " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, state );
            ps.setString( 2, transactionIdNotExist ? authorizeKey : transactionId );
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

//    public static void main( String[] args )
//    {
////        int p =  2147483647; //Integer Max value
//        ChgTimeStamp chgTimeStamp = new ChgTimeStamp(  );
//        chgTimeStamp.setYear( 2015 );
//        chgTimeStamp.setMonth( 2 );
//        chgTimeStamp.setDate( 22 );
//        chgTimeStamp.setHour( 5 );
//        chgTimeStamp.setMinute( 4 );
//        chgTimeStamp.setSecond( 7 );
//        System.out.println(chgTimeStamp);
//        System.out.println(chgTimeStamp.getLastTwoDigitsOfYear());
//        System.out.println(chgTimeStamp.getDayOfYear());
//        System.out.println(chgTimeStamp._getTimeValue());
//        System.out.println(generateTransactionID( chgTimeStamp, "0719028959" ));
//    }

    /**
     *
     * @param chgTimeStamp
     * @param phoneNum
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
//    private static int generateTransactionID(ChgTimeStamp chgTimeStamp, String phoneNum)
//    {
//        int phoneNumInt = Integer.parseInt( phoneNum );
//        System.out.println(phoneNumInt);
//        int modifyYearValue = chgTimeStamp.getLastTwoDigitsOfYear() * (int)Math.pow( 10,7 );
//        int modifyDayValue =  chgTimeStamp.getDayOfYear() * (int)Math.pow( 10,4 );
//        int modifyTimeValue = chgTimeStamp._getTimeValue();
//        int uniqueKeyFromDate = modifyYearValue + modifyDayValue + modifyTimeValue;
//        System.out.println(uniqueKeyFromDate);
//        return  uniqueKeyFromDate + phoneNumInt;
//    }

    /**
     *
     * @param chgTimeStamp
     * PhoneNum - Integer Value of phone Number
     * yearVal - ex : year is 2015 last 2 digits of year(15*pow(10,7) -> 150000000)
     * dayVal - ex : mar 22 -> 84 (day num of year max 366) -> 84*pow(10,4) -> 840000
     * final unique key- yearVal+ dayVal
     * 150000000
     *    840000
     *final -> 150840000
     * @return transactionID = uniqueDateKey + TRS_ID_COUNTER_PER_DAY
     */
    private static int generateTransactionID( ChgTimeStamp chgTimeStamp )
    {
        TRS_ID_COUNTER_PER_DAY++;
        if( TRS_ID_COUNTER_PER_DAY > MAXIMUM_TRS_ID_COUNTER )
        {
            TRS_ID_COUNTER_PER_DAY = 1;
        }
        int modifyYearValue = chgTimeStamp.getLastTwoDigitsOfYear() * (int) Math.pow( 10, 7 );
        int modifyDayValue = chgTimeStamp.getDayOfYear() * (int) Math.pow( 10, 4 );
        int uniqueKeyFromDate = modifyYearValue + modifyDayValue;
        return uniqueKeyFromDate + TRS_ID_COUNTER_PER_DAY;
    }

}
