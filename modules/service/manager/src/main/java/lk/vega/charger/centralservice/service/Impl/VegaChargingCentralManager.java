package lk.vega.charger.centralservice.service.Impl;

import lk.vega.charger.centralservice.service.*;

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
    @Override
    public AuthorizeResponse authorize( @WebParam(name = "authorizeRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") AuthorizeRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public StartTransactionResponse startTransaction( @WebParam(name = "startTransactionRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StartTransactionRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public StopTransactionResponse stopTransaction( @WebParam(name = "stopTransactionRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StopTransactionRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
