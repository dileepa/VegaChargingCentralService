package lk.vega.charger.centralservice.service.transaction.statecontroller;

import lk.vega.charger.centralservice.service.AuthorizeRequest;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentDetail;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentGateWay;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentGateWayFactory;
import lk.vega.charger.util.ChgResponse;

/**
 * Created by Dileepa on 3/21/15.
 */
public class TransactionBeginningState implements TransactionState
{


    @Override
    public ChgResponse proceedState(TransactionContext transactionContext)
    {
        ChgResponse res = null;
        AuthorizeRequest authorizeRequest = transactionContext.getAuthorizeRequest();
        PaymentDetail paymentDetail = PaymentGateWayFactory.decodeAuthorizationRequestToPaymentDetail( authorizeRequest );
        PaymentGateWay paymentGateWay = PaymentGateWayFactory.selectPaymentGateWay(paymentDetail);
        if( paymentGateWay != null )
        {
            res = PaymentGateWayFactory.doPayment( paymentDetail, paymentGateWay );
        }

        return res;
    }
}
