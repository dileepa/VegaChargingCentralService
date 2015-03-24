package lk.vega.charger.centralservice.service.transaction.statecontroller;

import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;
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
        ChgResponse chgResponse = CoreController.save( chargeTransaction );
        if( chgResponse.isSuccess() )
        {
            chgResponse.setReturnData( chargeTransaction );
        }
        return chgResponse;
    }
}
