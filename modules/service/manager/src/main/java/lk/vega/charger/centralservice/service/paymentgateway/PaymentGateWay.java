package lk.vega.charger.centralservice.service.paymentgateway;

import lk.vega.charger.util.VegaError;

/**
 * Created by dileepa on 3/19/15.
 */
public interface PaymentGateWay
{

    public void connect() throws Exception;

    public VegaError rollbackPayment(String authorizeKey) throws Exception;

    public VegaError validatePayment(String authorizeKey) throws Exception;

    public VegaError validatePaymentWithHold(String authorizeKey) throws Exception;

    public VegaError commitPayment(String authorizeKey) throws Exception;

    public VegaError refundPayment( String authorizeKey) throws Exception;


}
