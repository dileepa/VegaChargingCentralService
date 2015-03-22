package lk.vega.charger.centralservice.service.transaction.statecontroller;

import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.Savable;

/**
 * Created by Dileepa on 3/21/15.
 */
public class TransactionStoppedState implements TransactionState
{


    @Override
    public ChgResponse proceedState(TransactionContext transactionContext)
    {
        ChargeTransaction inProgressChargeTransaction = transactionContext.getChargeTransaction();
        inProgressChargeTransaction.setTransactionStatus( TransactionController.TRS_FINISHED );
        inProgressChargeTransaction.setStatus( Savable.MODIFIED );
        //TODO other attributes endtime..
        //TODO update query for save
        return null;
    }
}
