package lk.vega.charger.centralservice.service.paymentgateway;

import lk.vega.charger.util.ChgResponse;

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

    public ChgResponse connect(PaymentDetail paymentDetail) throws Exception;

    public ChgResponse rollbackPayment( PaymentDetail paymentDetail ) throws Exception;

    public ChgResponse validatePayment( PaymentDetail paymentDetail ) throws Exception;

    public ChgResponse validatePaymentWithHold( PaymentDetail paymentDetail ) throws Exception;

    public ChgResponse commitPayment( PaymentDetail paymentDetail ) throws Exception;

    public ChgResponse refundPayment( PaymentDetail paymentDetail ) throws Exception;

    public String getPaymentGateWayType();

}
