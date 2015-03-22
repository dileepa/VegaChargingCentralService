package lk.vega.charger.centralservice.service.transaction.statecontroller;

import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;

/**
 * Created by Dileepa on 3/21/15.
 */
public interface TransactionState
{
    public ChgResponse proceedState(TransactionContext transactionContext);

}
