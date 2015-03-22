package lk.vega.charger.centralservice.service.paymentgateway;

import lk.vega.charger.centralservice.service.AuthorizeRequest;
import lk.vega.charger.util.ChgResponse;

/**
 * Created by dileepa on 3/20/15.
 */
public class PaymentGateWayFactory
{
    public static final String DIALOG_UNIQUE_KEY = "077";
    public static final String MOBITEL_UNIQUE_KEY = "071";

    public static PaymentDetail decodeRequestToPaymentDetail( AuthorizeRequest parameters )
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

        return paymentGateWay;
    }

    public static ChgResponse doDummyPayment( PaymentDetail paymentDetail, PaymentGateWay paymentGateWay )
    {
        //TODO payment gate way dummy payment logic control here
        return null;
    }
}
