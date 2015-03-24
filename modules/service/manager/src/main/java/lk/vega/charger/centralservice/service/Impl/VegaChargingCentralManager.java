package lk.vega.charger.centralservice.service.Impl;

import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.centralservice.service.transaction.statecontroller.TransactionBeginningState;
import lk.vega.charger.centralservice.service.transaction.statecontroller.TransactionContext;
import lk.vega.charger.centralservice.service.transaction.statecontroller.TransactionProceedState;
import lk.vega.charger.centralservice.service.transaction.statecontroller.TransactionStartedState;
import lk.vega.charger.centralservice.service.transaction.statecontroller.TransactionState;
import lk.vega.charger.centralservice.service.transaction.statecontroller.TransactionStoppedState;
import lk.vega.charger.core.ChargeTransaction;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;
import ocpp.cs._2012._06.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.BindingType;

/**
 * Created with IntelliJ IDEA.
 * User: dileepa
 * Date: 3/18/15
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
@WebService(name = "CentralSystemService", targetNamespace = "urn://Ocpp/Cs/2012/06/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE, style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
@BindingType(value="http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
@XmlSeeAlso({
        ObjectFactory.class
})
public class VegaChargingCentralManager implements CentralSystemService
{

    @WebMethod(exclude = true) @PostConstruct
    public void initJaxWS()
    {
        try
        {
            CoreController.init();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        //TODO load service configurations.
    }

    @WebMethod(exclude = true) @PreDestroy
    public void destroy()
    {

    }

    /**
     * in this request, authorizeKey has particular format
     * phonenum#intialamount#timestamp
     * @param parameters
     * @return id  phonenum#intialamount#timestamp%crossReference
     */
    @WebMethod (action = "/Authorize")
    @Override
    public AuthorizeResponse authorize( @WebParam(name = "authorizeRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") AuthorizeRequest parameters )
    {
        String authorizeKey = parameters.getIdTag();
        ChgResponse chgResponse = TransactionController.loadProcessingTransaction( authorizeKey, TransactionController.TRS_STARTED );

        AuthorizeResponse authorizeResponse = new AuthorizeResponse();
        authorizeResponse.setIdTagInfo( new IdTagInfo() );
        IdTagInfo idTagInfo = authorizeResponse.getIdTagInfo();
        idTagInfo.setParentIdTag( parameters.getIdTag() );

        if( chgResponse.isSuccess() )
        {
            if( TransactionController.TRS_NEW.equals( chgResponse.getErrorCode() ) )
            {
                TransactionContext transactionContext = new TransactionContext();
                transactionContext.setAuthorizeRequest( parameters );
                TransactionState transactionBeginningState = new TransactionBeginningState();
                transactionContext.setTransactionState( transactionBeginningState );
                ChgResponse response = transactionContext.proceedState();
                if( response.isError() )
                {
                    idTagInfo.setStatus( AuthorizationStatus.BLOCKED );
                }
                else if( response.isSuccess() )
                {
                    String paymentGatewayCrossRef = (String) response.getReturnData();
                    idTagInfo.setStatus( AuthorizationStatus.ACCEPTED );
                    StringBuilder sb = new StringBuilder();
                    sb.append( parameters.getIdTag() );
                    sb.append( TransactionController.TRS_CROSS_REF_SPLITTER );//use for separate the cross reference of payment gate way.
                    sb.append( paymentGatewayCrossRef );
                    idTagInfo.setParentIdTag( sb.toString() );
                }
            }
            else
            {
                ChargeTransaction inProgressChargeTransaction = (ChargeTransaction) chgResponse.getReturnData();
                TransactionContext transactionContext = new TransactionContext();
                transactionContext.setAuthorizeRequest( parameters );
                transactionContext.setChargeTransaction( inProgressChargeTransaction );
                TransactionState transactionStartedState = new TransactionProceedState();
                transactionContext.setTransactionState( transactionStartedState );
                ChgResponse res = transactionContext.proceedState();
                if( res.isError() )
                {
                    idTagInfo.setStatus( AuthorizationStatus.BLOCKED );
                }
                else if( res.isSuccess() )
                {
                    idTagInfo.setStatus( AuthorizationStatus.ACCEPTED );
                    StringBuilder sb = new StringBuilder();
                    sb.append( inProgressChargeTransaction.getAuthenticationKey() );
                    sb.append( TransactionController.TRS_CROSS_REF_SPLITTER );//use for separate the cross reference of payment gate way.
                    sb.append( inProgressChargeTransaction.getCrossReference() );
                    idTagInfo.setParentIdTag( sb.toString() );
                }
            }
        }
        else if( chgResponse.isError() )
        {
            idTagInfo.setStatus( AuthorizationStatus.BLOCKED );
        }
        return authorizeResponse;
    }

    /**
     * in this request, authorizeKey has particular format
     * phonenum#intialamount#timestamp%crossRef
     * @param parameters
     * @return id  trsID
     */
    @WebMethod (action = "/StartTransaction")
    @Override
    public StartTransactionResponse startTransaction( @WebParam(name = "startTransactionRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StartTransactionRequest parameters )
    {
        StartTransactionResponse startTransactionResponse = new StartTransactionResponse();
        startTransactionResponse.setIdTagInfo( new IdTagInfo() );
        IdTagInfo idTagInfo = startTransactionResponse.getIdTagInfo();
        idTagInfo.setParentIdTag( parameters.getIdTag() );

        TransactionContext transactionContext = new TransactionContext();
        transactionContext.setStartTransactionRequest(parameters);
        TransactionState transactionStartedState = new TransactionStartedState();
        transactionContext.setTransactionState(transactionStartedState);
        ChgResponse chgResponse = transactionContext.proceedState();
        if (chgResponse.isSuccess())
        {
            ChargeTransaction chargeTransaction = (ChargeTransaction)chgResponse.getReturnData();
            idTagInfo.setStatus( AuthorizationStatus.ACCEPTED );
            startTransactionResponse.setTransactionId( 123 );  //TODO need to remove after transactionId generation logic implementation
        }
        else
        {
            idTagInfo.setStatus( AuthorizationStatus.BLOCKED );
            startTransactionResponse.setTransactionId( -1 );
        }
        return startTransactionResponse;
    }

    /**
     * in this request, authorizeKey has particular format
     * phonenum#intialamount#timestamp%crossRef
     * @param parameters
     * @return id  trsID
     */
    @WebMethod (action = "/StopTransaction")
    @Override
    public StopTransactionResponse stopTransaction( @WebParam(name = "stopTransactionRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StopTransactionRequest parameters )
    {
        StopTransactionResponse stopTransactionResponse = new StopTransactionResponse();
        stopTransactionResponse.setIdTagInfo( new IdTagInfo() );
        IdTagInfo idTagInfo = stopTransactionResponse.getIdTagInfo();

        String authorizeKeyWithCrossRef = parameters.getIdTag();
        String []authKeyCrossRefArray = TransactionController.phoneNumAmountAndCrossRefSeparator( authorizeKeyWithCrossRef );
        String authorizeKey = authKeyCrossRefArray[0];
        ChgResponse response = TransactionController.loadProcessingTransaction( authorizeKey, TransactionController.TRS_PROCESSED );
        if (response.isSuccess())
        {
            ChargeTransaction inProgressChargeTransaction = (ChargeTransaction)response.getReturnData();
            if (inProgressChargeTransaction != null)
            {
                TransactionContext transactionContext = new TransactionContext();
                transactionContext.setChargeTransaction(inProgressChargeTransaction);
                transactionContext.setStopTransactionRequest( parameters );
                TransactionState transactionStartedState = new TransactionStoppedState();
                transactionContext.setTransactionState(transactionStartedState);
                ChgResponse chgResponse = transactionContext.proceedState();
                if (chgResponse.isSuccess())
                {
                    idTagInfo.setStatus( AuthorizationStatus.ACCEPTED );
                    idTagInfo.setParentIdTag( parameters.getIdTag() );
                }
                else
                {
                    idTagInfo.setStatus( AuthorizationStatus.BLOCKED );
                    idTagInfo.setParentIdTag( parameters.getIdTag() );
                }
            }
        }
        return stopTransactionResponse;
    }

    @WebMethod (action = "/Heartbeat")
    @Override
    public HeartbeatResponse heartbeat( @WebParam(name = "heartbeatRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") HeartbeatRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @WebMethod (action = "/MeterValues")
    @Override
    public MeterValuesResponse meterValues( @WebParam(name = "meterValuesRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") MeterValuesRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @WebMethod (action = "/BootNotification")
    @Override
    public BootNotificationResponse bootNotification( @WebParam(name = "bootNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") BootNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @WebMethod (action = "/StatusNotification")
    @Override
    public StatusNotificationResponse statusNotification( @WebParam(name = "statusNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StatusNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @WebMethod (action = "/FirmwareStatusNotification")
    @Override
    public FirmwareStatusNotificationResponse firmwareStatusNotification( @WebParam(name = "firmwareStatusNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") FirmwareStatusNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @WebMethod (action = "/DiagnosticsStatusNotification")
    @Override
    public DiagnosticsStatusNotificationResponse diagnosticsStatusNotification( @WebParam(name = "diagnosticsStatusNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") DiagnosticsStatusNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @WebMethod (action = "/DataTransfer")
    @Override
    public DataTransferResponse dataTransfer( @WebParam(name = "dataTransferRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") DataTransferRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
