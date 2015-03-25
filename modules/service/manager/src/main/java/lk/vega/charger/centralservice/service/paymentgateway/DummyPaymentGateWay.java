package lk.vega.charger.centralservice.service.paymentgateway;

import lk.vega.charger.util.ChgResponse;

/**
 * Created by dileepa on 3/23/15.
 */
public class DummyPaymentGateWay implements PaymentGateWay
{


    @Override
    public ChgResponse connect(PaymentDetail paymentDetail) throws Exception {
        ChgResponse chgResponse = new ChgResponse( ChgResponse.SUCCESS, "Successfully Connected." );
        return chgResponse;
    }

    @Override public ChgResponse rollbackPayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public ChgResponse validatePayment( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse( ChgResponse.SUCCESS, "Validation Success" );
        return chgResponse;
    }

    @Override public ChgResponse validatePaymentWithHold( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse( ChgResponse.SUCCESS, "Payment Hold Success" );
        return chgResponse;
    }

    @Override public ChgResponse commitPayment( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse( ChgResponse.SUCCESS, "Payment is committed Successfully" );
        chgResponse.setReturnData( "CROSS-REF" );
        return chgResponse;
    }

    @Override public ChgResponse refundPayment( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse( ChgResponse.SUCCESS, "Refund is successfully done" );
        return chgResponse;
    }

    @Override public String getPaymentGateWayType()
    {
        return PaymentGateWayFactory.DUMMY;
    }
}
