package lk.vega.charger.centralservice.service.paymentgateway;

import lk.vega.charger.util.VegaError;

/**
 * Created by dileepa on 3/19/15.
 */
public interface PaymentGateWay
{
    /*
    1.first create connection - connect
    2. validate payment details (ex, correct phone number and amount) - validatePayment
    3. Hold initial payment - validatePaymentWithHold
    4. commit actual payment - commitPayment
    5. refund rest of payment - refundPayment
    6. if u got error need to rollback it - rollbackPayment
     */

    public void connect() throws Exception;

    public VegaError rollbackPayment( PaymentDetail paymentDetail ) throws Exception;

    public VegaError validatePayment( PaymentDetail paymentDetail ) throws Exception;

    public VegaError validatePaymentWithHold( PaymentDetail paymentDetail ) throws Exception;

    public VegaError commitPayment( PaymentDetail paymentDetail ) throws Exception;

    public VegaError refundPayment( PaymentDetail paymentDetail ) throws Exception;



}
