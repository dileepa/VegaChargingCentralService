package lk.vega.charger.centralservice.service.transaction;

import lk.vega.charger.centralservice.service.StartTransactionRequest;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;

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


    public static ChargeTransaction generateTransaction( StartTransactionRequest parameters )
    {
        //TODO separate idTagInfo - it include both authenkey and crossRef
        ChargeTransaction chargeTransaction = new ChargeTransaction();
        chargeTransaction.init();
        chargeTransaction.setPointId( parameters.getConnectorId() );
        chargeTransaction.setAuthenticationKey( "" ); //TODO set key after separation
        chargeTransaction.setCrossReference( "" ); //TODO set cross ref after separation
        chargeTransaction._setMeterStart( parameters.getMeterStart() );
        chargeTransaction.setReservationId( parameters.getReservationId() );
        chargeTransaction.setStatus( Savable.NEW );
        chargeTransaction.setTransactionId( "" ); //TODO implement Trs id generation logic and set it.



        return chargeTransaction;

    }

    public static ChargeTransaction loadProcessingTransaction( String authorizeKey, String state )
    {
        //TODO db loading part.
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            if (state.equals( rs.getString( "STATUS" ) ))
            {
                ChargeTransaction chargeTransaction = new ChargeTransaction();
                chargeTransaction.init();
                chargeTransaction.load( rs,con,0 );
                return chargeTransaction;

            }

        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            DBUtility.close( rs );
            DBUtility.close( ps );
            DBUtility.close( con );
        }
        return null;
    }

}
