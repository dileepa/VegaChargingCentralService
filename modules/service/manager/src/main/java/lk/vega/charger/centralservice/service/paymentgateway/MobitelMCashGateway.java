package lk.vega.charger.centralservice.service.paymentgateway;

import lk.vega.charger.util.ChgResponse;

/**
 * Created by dileepa on 3/19/15.
 */
public class MobitelMCashGateway implements PaymentGateWay
{


    @Override public ChgResponse connect() throws Exception
    {
        return null;
    }

    @Override public ChgResponse rollbackPayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public ChgResponse validatePayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public ChgResponse validatePaymentWithHold( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public ChgResponse commitPayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public ChgResponse refundPayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public String getPaymentGateWayType()
    {
        return PaymentGateWayFactory.MOBITEL;
    }

}
