package lk.vega.charger.centralservice.service.Impl;

import lk.vega.charger.centralservice.service.*;
import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.centralservice.service.transaction.statecontroller.*;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;

import javax.jws.WebParam;

/**
 * Created with IntelliJ IDEA.
 * User: dileepa
 * Date: 3/18/15
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class VegaChargingCentralManager implements CentralSystemService
{

    /**
     * in this request, authorizeKey has particular format
     * phonenum#intialamount@timestamp
     * @param parameters
     * @return
     */

    @Override
    public AuthorizeResponse authorize( @WebParam(name = "authorizeRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") AuthorizeRequest parameters )
    {
        String authorizeKey = parameters.getIdTag();
        ChargeTransaction inProgressChargeTransaction = TransactionController.loadProcessingTransaction( authorizeKey, TransactionController.TRS_STARTED );

        AuthorizeResponse authorizeResponse = new AuthorizeResponse();
        IdTagInfo idTagInfo = authorizeResponse.getIdTagInfo();
        idTagInfo.setParentIdTag( parameters.getIdTag() );

        ChgResponse error = null;
        if( TransactionController.TRS_NEW.equals(inProgressChargeTransaction.getTransactionStatus()) )
        {
            TransactionContext transactionContext = new TransactionContext();
            transactionContext.setAuthorizeRequest(parameters);
            TransactionState transactionBeginningState = new TransactionBeginningState();
            transactionContext.setTransactionState(transactionBeginningState);
            ChgResponse response = transactionContext.proceedState();
            if( error.isError() )
            {
                idTagInfo.setStatus( AuthorizationStatus.BLOCKED );
                //TODO control the logic here according to vegaError status and message.
            }
            else if( error.isSuccess() )
            {
                idTagInfo.setStatus( AuthorizationStatus.ACCEPTED );
                idTagInfo.setParentIdTag( parameters.getIdTag() +"%-crossRef-" );//TODO need append initial cross reference.
            }
        }
        else if (TransactionController.TRS_STARTED.equals(inProgressChargeTransaction.getTransactionStatus()))
        {
            TransactionContext transactionContext = new TransactionContext();
            transactionContext.setAuthorizeRequest(parameters);
            transactionContext.setChargeTransaction(inProgressChargeTransaction);
            TransactionState transactionStartedState = new TransactionProceedState();
            transactionContext.setTransactionState(transactionStartedState);
            ChgResponse chgResponse = transactionContext.proceedState();
            //TODO get ChargeTransaction data from chgResponse
            //TODO modify AuthorizeResponse
        }
        else
        {
            //TODO handle invalid one....
        }

        return authorizeResponse;
    }

    @Override
    public StartTransactionResponse startTransaction( @WebParam(name = "startTransactionRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StartTransactionRequest parameters )
    {
        TransactionContext transactionContext = new TransactionContext();
        transactionContext.setStartTransactionRequest(parameters);
        TransactionState transactionStartedState = new TransactionStartedState();
        transactionContext.setTransactionState(transactionStartedState);
        ChgResponse chgResponse = transactionContext.proceedState();
        //TODO get ChargeTransaction data from chgResponse
        StartTransactionResponse startTransactionResponse = new StartTransactionResponse();
        //TODO Mapping StartTransactionResponse from  ChargeTransaction
        return startTransactionResponse;
    }

    @Override
    public StopTransactionResponse stopTransaction( @WebParam(name = "stopTransactionRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StopTransactionRequest parameters )
    {
        String authorizeKey = parameters.getIdTag();
        ChargeTransaction inProgressChargeTransaction = TransactionController.loadProcessingTransaction( authorizeKey, TransactionController.TRS_PROCESSED );
        StopTransactionResponse stopTransactionResponse = new StopTransactionResponse();
        if (inProgressChargeTransaction != null)
        {
            TransactionContext transactionContext = new TransactionContext();
            transactionContext.setChargeTransaction(inProgressChargeTransaction);
            TransactionState transactionStartedState = new TransactionStoppedState();
            transactionContext.setTransactionState(transactionStartedState);
            ChgResponse chgResponse = transactionContext.proceedState();
            //TODO get ChargeTransaction data from chgResponse
            //TODO modify StopTransactionResponse
        }

        return stopTransactionResponse;
    }

    @Override
    public HeartbeatResponse heartbeat( @WebParam(name = "heartbeatRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") HeartbeatRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MeterValuesResponse meterValues( @WebParam(name = "meterValuesRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") MeterValuesRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BootNotificationResponse bootNotification( @WebParam(name = "bootNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") BootNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public StatusNotificationResponse statusNotification( @WebParam(name = "statusNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StatusNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FirmwareStatusNotificationResponse firmwareStatusNotification( @WebParam(name = "firmwareStatusNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") FirmwareStatusNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DiagnosticsStatusNotificationResponse diagnosticsStatusNotification( @WebParam(name = "diagnosticsStatusNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") DiagnosticsStatusNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DataTransferResponse dataTransfer( @WebParam(name = "dataTransferRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") DataTransferRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
