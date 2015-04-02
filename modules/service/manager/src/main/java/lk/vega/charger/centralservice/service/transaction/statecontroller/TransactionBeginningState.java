package lk.vega.charger.centralservice.service.transaction.statecontroller;

import lk.vega.charger.centralservice.service.paymentgateway.PaymentDetail;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentGateWay;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentGateWayFactory;
import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.core.ChargeAuthKey;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.ChgTimeStamp;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.Savable;
import ocpp.cs._2012._06.AuthorizeRequest;

/**
 * Created by Dileepa on 3/21/15.
 */
public class TransactionBeginningState implements TransactionState
{


    @Override
    public ChgResponse proceedState(TransactionContext transactionContext)
    {
        ChgResponse res = null;
        ChgResponse saveResponse = null;
        AuthorizeRequest authorizeRequest = transactionContext.getAuthorizeRequest();
        PaymentDetail paymentDetail = PaymentGateWayFactory.decodeAuthorizationRequestToPaymentDetail( authorizeRequest );
        PaymentGateWay paymentGateWay = PaymentGateWayFactory.selectPaymentGateWay(paymentDetail);
        if( paymentGateWay != null )
        {
            res = PaymentGateWayFactory.doPayment( paymentDetail, paymentGateWay );
        }

        if ( res.isSuccess() )
        {
            String paymentGatewayCrossRef = (String) res.getReturnData();
            StringBuilder sb = new StringBuilder();
            sb.append( authorizeRequest.getIdTag() );
            sb.append( TransactionController.TRS_CROSS_REF_SPLITTER );
            sb.append( paymentGatewayCrossRef );
            ChargeAuthKey chargeAuthKey = new ChargeAuthKey();
            chargeAuthKey.setStatus( Savable.NEW );
            chargeAuthKey.setAuthKey( sb.toString() );
            chargeAuthKey.setStartTime( new ChgTimeStamp(  ) );
            int startSeconds = chargeAuthKey.getStartTime().getSecond();
            ChgTimeStamp endTime = new ChgTimeStamp(  );
            endTime.setSecond( startSeconds + CoreController.AUTH_KEY_EXPIRE_TIMEOUT_VAL );
            chargeAuthKey.setEndTime( endTime );
            saveResponse = CoreController.save( chargeAuthKey );
            if (saveResponse.isError() )
            {
                res = saveResponse;
            }
        }

        return res;
    }
}
