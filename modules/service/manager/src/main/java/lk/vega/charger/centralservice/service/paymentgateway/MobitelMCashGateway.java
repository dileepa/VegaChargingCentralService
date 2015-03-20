package lk.vega.charger.centralservice.service.paymentgateway;

import lk.vega.charger.util.VegaError;

/**
 * Created by dileepa on 3/19/15.
 */
public class MobitelMCashGateway implements PaymentGateWay
{


    @Override public void connect() throws Exception
    {

    }

    @Override public VegaError rollbackPayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public VegaError validatePayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public VegaError validatePaymentWithHold( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public VegaError commitPayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public VegaError refundPayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

}
