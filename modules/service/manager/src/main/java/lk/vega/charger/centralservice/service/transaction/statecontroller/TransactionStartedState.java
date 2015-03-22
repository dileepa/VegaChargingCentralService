package lk.vega.charger.centralservice.service.transaction.statecontroller;

import lk.vega.charger.centralservice.service.StartTransactionRequest;
import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;

/**
 * Created by Dileepa on 3/21/15.
 */
public class TransactionStartedState implements TransactionState
{


    @Override
    public ChgResponse proceedState(TransactionContext transactionContext)
    {
        StartTransactionRequest startTransactionRequest = transactionContext.getStartTransactionRequest();
        ChargeTransaction chargeTransaction = TransactionController.generateTransaction( startTransactionRequest );
        chargeTransaction.setTransactionStatus(TransactionController.TRS_STARTED );
        //TODO ChargeTransaction Save Call Should be here..
        return  null;
    }
}
