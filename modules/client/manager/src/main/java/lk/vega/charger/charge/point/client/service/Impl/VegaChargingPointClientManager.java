package lk.vega.charger.charge.point.client.service.Impl;

import ocpp.cp._2012._06.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/28/15.
 * Time on 11:23 AM
 */
@WebService(name = "ChargePointClientManager", targetNamespace = "urn://Ocpp/Cp/2012/06/")
public class VegaChargingPointClientManager implements ChargePointService
{
    @WebMethod
    @Override
    public UnlockConnectorResponse unlockConnector( @WebParam(name = "unlockConnectorRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") UnlockConnectorRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public ResetResponse reset( @WebParam(name = "resetRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") ResetRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public ChangeAvailabilityResponse changeAvailability( @WebParam(name = "changeAvailabilityRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") ChangeAvailabilityRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public GetDiagnosticsResponse getDiagnostics( @WebParam(name = "getDiagnosticsRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") GetDiagnosticsRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public ClearCacheResponse clearCache( @WebParam(name = "clearCacheRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") ClearCacheRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public UpdateFirmwareResponse updateFirmware( @WebParam(name = "updateFirmwareRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") UpdateFirmwareRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public ChangeConfigurationResponse changeConfiguration( @WebParam(name = "changeConfigurationRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") ChangeConfigurationRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public RemoteStartTransactionResponse remoteStartTransaction( @WebParam(name = "remoteStartTransactionRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") RemoteStartTransactionRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public RemoteStopTransactionResponse remoteStopTransaction( @WebParam(name = "remoteStopTransactionRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") RemoteStopTransactionRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public CancelReservationResponse cancelReservation( @WebParam(name = "cancelReservationRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") CancelReservationRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public DataTransferResponse dataTransfer( @WebParam(name = "dataTransferRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") DataTransferRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public GetConfigurationResponse getConfiguration( @WebParam(name = "getConfigurationRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") GetConfigurationRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public GetLocalListVersionResponse getLocalListVersion( @WebParam(name = "getLocalListVersionRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") GetLocalListVersionRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public ReserveNowResponse reserveNow( @WebParam(name = "reserveNowRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") ReserveNowRequest parameters )
    {
        return null;
    }

    @WebMethod
    @Override
    public SendLocalListResponse sendLocalList( @WebParam(name = "sendLocalListRequest", targetNamespace = "urn://Ocpp/Cp/2012/06/", partName = "parameters") SendLocalListRequest parameters )
    {
        return null;
    }
}
