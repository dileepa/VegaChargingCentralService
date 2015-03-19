package lk.vega.charger.centralservice.service.transaction;

import lk.vega.charger.centralservice.service.StartTransactionRequest;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.DBUtility;

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


    public static ChargeTransaction generateTransaction( StartTransactionRequest parameters )
    {

        ChargeTransaction chargeTransaction = new ChargeTransaction();
        chargeTransaction.init();
        chargeTransaction.setPointId( parameters.getConnectorId() );
        chargeTransaction.setAuthenticationKey( parameters.getIdTag() );
        chargeTransaction._setMeterStart( parameters.getMeterStart() );
        chargeTransaction.setReservationId( parameters.getReservationId() );
        chargeTransaction.setTransactionStatus( TRS_STARTED );
        //TODO update the ChargeTransaction status here.
        //TODO Handle Transaction ID generation Logic here
        //TODO ChargeTransaction Save Call Should be here..


        return chargeTransaction;

    }

    public static ChargeTransaction loadProcessingTransaction( String authorizeKey, String state )
    {
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
