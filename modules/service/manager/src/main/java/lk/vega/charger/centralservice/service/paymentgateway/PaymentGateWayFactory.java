package lk.vega.charger.centralservice.service.paymentgateway;

import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;
import ocpp.cs._2012._06.AuthorizeRequest;

/**
 * Created by dileepa on 3/20/15.
 */
public class PaymentGateWayFactory
{
    public static final String DIALOG_UNIQUE_KEY = "077";
    public static final String DIALOG_UNIQUE_KEY_1 = "076";
    public static final String MOBITEL_UNIQUE_KEY = "071";
    public static final String DIALOG = "DIALOG";
    public static final String MOBITEL = "MOBITEL";
    public static final String DUMMY = "DUMMY";

    public static PaymentDetail decodeAuthorizationRequestToPaymentDetail( AuthorizeRequest parameters )
    {
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.init();
        paymentDetail.setAuthenticationKey( parameters.getIdTag() );
        return paymentDetail;
    }

    public static PaymentDetail decodeStopTransactionRequestToPaymentDetail( ChargeTransaction parameters )
    {
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.init();
        paymentDetail.setTransactionKey( parameters.getPaymentGateWayType() );
        StringBuilder newAuthKey = new StringBuilder(  );
        String phoneAmountArray [] = TransactionController.phoneNumAmountAndDateSeparator( parameters.getAuthenticationKey() );
        newAuthKey.append( phoneAmountArray[0] );
        newAuthKey.append( TransactionController.TRS_AUTH_KEY_SPLITTER );
        newAuthKey.append( parameters.getFinalAmount() );
        newAuthKey.append( TransactionController.TRS_AUTH_KEY_SPLITTER );
        newAuthKey.append( parameters.getTransactionId() );
        paymentDetail.setAuthenticationKey( newAuthKey.toString() );
        return paymentDetail;
    }

    public static PaymentGateWay selectPaymentGateWay( PaymentDetail paymentDetail )
    {
        PaymentGateWay paymentGateWay = null;
        String authKey = paymentDetail.getAuthenticationKey();
        boolean validAuthKey = authKey != null && authKey.length() != 0;
        if( ( validAuthKey && (authKey.startsWith( DIALOG_UNIQUE_KEY )||authKey.startsWith( DIALOG_UNIQUE_KEY_1 )) ) || DIALOG.equals( paymentDetail.getTransactionKey() ) )
        {
            paymentGateWay = new DialogEasyCashGateway();
        }
        else if( ( validAuthKey && authKey.startsWith( MOBITEL_UNIQUE_KEY ) ) || MOBITEL.equals( paymentDetail.getTransactionKey() ) )
        {
            paymentGateWay = new MobitelMCashGateway();
        }
        else
        {
            paymentGateWay = new DummyPaymentGateWay();
        }

        return paymentGateWay;
    }

    public static String selectPaymentGateWayType( String authKey )
    {
        String paymentGateWayType = "";
        if( authKey.startsWith( DIALOG_UNIQUE_KEY ) || authKey.startsWith( DIALOG_UNIQUE_KEY_1 ) )
        {
            paymentGateWayType = DIALOG;
        }
        else if( authKey.startsWith( MOBITEL_UNIQUE_KEY ) )
        {
            paymentGateWayType = MOBITEL;
        }
        else
        {
            paymentGateWayType = DUMMY;
        }

        return paymentGateWayType;
    }

    public static ChgResponse doPayment( PaymentDetail paymentDetail, PaymentGateWay paymentGateWay )
    {
        ChgResponse connectResponse = null;
        ChgResponse validatePaymentResponse = null;
        ChgResponse validatePaymentWithHoldResponse = null;
        ChgResponse commitResponse = null;
        ChgResponse returnResponse = new ChgResponse();
        try
        {
            connectResponse = paymentGateWay.connect(paymentDetail);
            if( connectResponse.isSuccess() )
            {
                validatePaymentResponse = paymentGateWay.validatePayment( paymentDetail );
                if( validatePaymentResponse.isSuccess() )
                {
                    validatePaymentWithHoldResponse = paymentGateWay.validatePaymentWithHold( paymentDetail );
                    if( validatePaymentWithHoldResponse.isSuccess() )
                    {
                        commitResponse = paymentGateWay.commitPayment( paymentDetail );
                        if( commitResponse.isError() )
                        {
                            returnResponse = paymentGateWay.rollbackPayment( paymentDetail );
                        }
                        returnResponse = commitResponse;
                    }
                    else
                    {
                        returnResponse = validatePaymentWithHoldResponse;
                    }
                }
                else
                {
                    returnResponse = validatePaymentResponse;
                }
            }
            else
            {
                returnResponse = connectResponse;
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        return returnResponse;
    }
}
