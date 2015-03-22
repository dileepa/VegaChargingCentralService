package lk.vega.charger.centralservice.service.transaction.statecontroller;

import lk.vega.charger.centralservice.service.StartTransactionRequest;
import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;

import java.sql.SQLException;

/**
 * Created by Dileepa on 3/21/15.
 */
public class TransactionStartedState implements TransactionState
{


    @Override
    public ChgResponse proceedState(TransactionContext transactionContext)
    {
        ChgResponse chgResponse = null;
        StartTransactionRequest startTransactionRequest = transactionContext.getStartTransactionRequest();
        ChargeTransaction chargeTransaction = TransactionController.generateTransaction( startTransactionRequest );
        chargeTransaction.setTransactionStatus(TransactionController.TRS_STARTED );
        try
        {
            chgResponse = CoreController.save(chargeTransaction);
        }
        catch (SQLException sq)
        {
            sq.printStackTrace();
            chgResponse = new ChgResponse(ChgResponse.ERROR,sq.getMessage());
        }
        catch (Exception e)
        {
           e.printStackTrace();
            chgResponse = new ChgResponse(ChgResponse.ERROR,e.getMessage())
        }
        //TODO ChargeTransaction Save Call Should be here..
        return  chgResponse;
    }
}
