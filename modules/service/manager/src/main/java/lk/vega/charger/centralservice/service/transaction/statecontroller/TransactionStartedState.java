package lk.vega.charger.centralservice.service.transaction.statecontroller;

import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.core.ChargeAuthKey;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.Savable;
import ocpp.cs._2012._06.StartTransactionRequest;

/**
 * Created by Dileepa on 3/21/15.
 */
public class TransactionStartedState implements TransactionState
{


    @Override
    public ChgResponse proceedState( TransactionContext transactionContext )
    {
        StartTransactionRequest startTransactionRequest = transactionContext.getStartTransactionRequest();
        ChargeTransaction chargeTransaction = TransactionController.generateTransaction( startTransactionRequest );
        ChargeAuthKey chargeAuthKey = transactionContext.getValidChargeAuthKey();
        chargeAuthKey.setStatus( Savable.DELETED );
        CoreController.save( chargeAuthKey );
        ChgResponse chgResponse = CoreController.save( chargeTransaction );
        if( chgResponse.isSuccess() )
        {
            ChgResponse trsLoadingRes = TransactionController.loadSpecificTransaction( chargeTransaction.getTransactionId() );
            if (trsLoadingRes.isSuccess())
            {
                chgResponse.setReturnData( (ChargeTransaction)trsLoadingRes.getReturnData() );
            }
        }
        return chgResponse;
    }
}
