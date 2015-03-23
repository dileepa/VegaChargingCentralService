package lk.vega.charger.centralservice.service.paymentgateway;

import lk.vega.charger.centralservice.service.AuthorizeRequest;
import lk.vega.charger.centralservice.service.StopTransactionRequest;
import lk.vega.charger.util.ChgResponse;

/**
 * Created by dileepa on 3/20/15.
 */
public class PaymentGateWayFactory
{
    public static final String DIALOG_UNIQUE_KEY = "077";
    public static final String MOBITEL_UNIQUE_KEY = "071";

    public static PaymentDetail decodeAuthorizationRequestToPaymentDetail( AuthorizeRequest parameters )
    {
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.init();
        paymentDetail.setAuthenticationKey( parameters.getIdTag() );
        return paymentDetail;
    }

    public static PaymentDetail decodeStopTransactionRequestToPaymentDetail( StopTransactionRequest parameters )
    {
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.init();
        paymentDetail.setAuthenticationKey( parameters.getIdTag() );
        return paymentDetail;
    }

    public static PaymentGateWay selectPaymentGateWay( PaymentDetail paymentDetail )
    {
        PaymentGateWay paymentGateWay = null;
        if( paymentDetail.getAuthenticationKey().startsWith( DIALOG_UNIQUE_KEY ) )
        {
            paymentGateWay = new DialogEasyCashGateway();
        }
        else if( paymentDetail.getAuthenticationKey().startsWith( MOBITEL_UNIQUE_KEY ) )
        {
            paymentGateWay = new MobitelMCashGateway();
        }
        else
        {
            paymentGateWay = new DummyPaymentGateWay();
        }

        return paymentGateWay;
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
            connectResponse = paymentGateWay.connect();
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
