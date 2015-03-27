package lk.vega.charger.centralservice.service.transaction.statecontroller;

import ocpp.cs._2012._06.*;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentDetail;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentGateWay;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentGateWayFactory;
import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.ChgTimeStamp;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.Savable;

/**
 * Created by Dileepa on 3/21/15.
 */
public class TransactionStoppedState implements TransactionState
{


    @Override
    public ChgResponse proceedState(TransactionContext transactionContext)
    {
        StopTransactionRequest stopTransactionRequest = transactionContext.getStopTransactionRequest();
        ChargeTransaction inProgressChargeTransaction = transactionContext.getChargeTransaction();
        inProgressChargeTransaction.setTransactionStatus( TransactionController.TRS_FINISHED );
        inProgressChargeTransaction.setStatus( Savable.MODIFIED );
        inProgressChargeTransaction.setEndTime( new ChgTimeStamp() );
        inProgressChargeTransaction.setMeterEnd( stopTransactionRequest.getMeterStop() );
        inProgressChargeTransaction.setFinalAmount( 25.0d );   //TODO final amount and energy consumption calculation.
        inProgressChargeTransaction.setEnergyConsumption( 0.0d );

        ChgResponse res = null;
        PaymentDetail paymentDetail = PaymentGateWayFactory.decodeStopTransactionRequestToPaymentDetail( inProgressChargeTransaction );
        PaymentGateWay paymentGateWay = PaymentGateWayFactory.selectPaymentGateWay(paymentDetail);
        if( paymentGateWay != null )
        {
            res = PaymentGateWayFactory.refundPayment( paymentDetail, paymentGateWay );
        }
        if (res.isSuccess())
        {
            inProgressChargeTransaction.setCrossReference( (String)res.getReturnData() );
            res = CoreController.save( inProgressChargeTransaction );
        }

        return res;
    }
}
