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

    @Override public VegaError rollbackPayment( String authorizeKey ) throws Exception
    {
        return null;
    }

    @Override public VegaError validatePayment( String authorizeKey ) throws Exception
    {
        return null;
    }

    @Override public VegaError validatePaymentWithHold( String authorizeKey ) throws Exception
    {
        return null;
    }

    @Override public VegaError commitPayment( String authorizeKey ) throws Exception
    {
        return null;
    }

    @Override public VegaError refundPayment( String authorizeKey ) throws Exception
    {
        return null;
    }
}
