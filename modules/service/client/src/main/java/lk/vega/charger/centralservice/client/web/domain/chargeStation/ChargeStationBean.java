package lk.vega.charger.centralservice.client.web.domain.chargeStation;

import lk.vega.charger.centralservice.client.web.dataLoader.loacation.LocationLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.location.LocationBean;
import lk.vega.charger.core.ChargeLocation;
import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.util.ChgDate;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.ChgTimeStamp;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/24/15.
 * Time on 1:16 PM
 */
public class ChargeStationBean extends DomainBeanImpl
{
    private int chargePointId;
    private String reference;
    private int locationId;
    private double power;
    private String type;
    private String protocol;
    private String firmwareVersion;
    private String hardwareVersion;
    private ChgDate lastUpdateDate;
    private ChgTimeStamp lastUpdateTimeStamp;
    private String userName;
    private LocationBean chargeLocationBean;
    private String machineUniqueRef;
    private String chargePointAvailabilityStatus;
    private String chargePointPowerStatus;


    public LocationBean getChargeLocationBean() {
        return chargeLocationBean;
    }

    public void setChargeLocationBean(LocationBean chargeLocationBean) {
        this.chargeLocationBean = chargeLocationBean;
    }

    public String getMachineUniqueRef()
    {
        return machineUniqueRef;
    }

    public void setMachineUniqueRef( String machineUniqueRef )
    {
        this.machineUniqueRef = machineUniqueRef;
    }

    public int getChargePointId()
    {
        return chargePointId;
    }

    public void setChargePointId( int chargePointId )
    {
        this.chargePointId = chargePointId;
    }

    public ChgDate getLastUpdateDate()
    {
        return lastUpdateDate;
    }

    public void setLastUpdateDate( ChgDate lastUpdateDate )
    {
        this.lastUpdateDate = lastUpdateDate;
    }

    public ChgTimeStamp getLastUpdateTimeStamp()
    {
        return lastUpdateTimeStamp;
    }

    public void setLastUpdateTimeStamp( ChgTimeStamp lastUpdateTimeStamp )
    {
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
    }

    public int getLocationId()
    {
        return locationId;
    }

    public void setLocationId( int locationId )
    {
        this.locationId = locationId;
    }

    public double getPower()
    {
        return power;
    }

    public void setPower( double power )
    {
        this.power = power;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol( String protocol )
    {
        this.protocol = protocol;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference( String reference )
    {
        this.reference = reference;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userId )
    {
        this.userName = userId;
    }

    public String getFirmwareVersion()
    {
        return firmwareVersion;
    }

    public void setFirmwareVersion( String firmwareVersion )
    {
        this.firmwareVersion = firmwareVersion;
    }

    public String getHardwareVersion()
    {
        return hardwareVersion;
    }

    public void setHardwareVersion( String hardwareVersion )
    {
        this.hardwareVersion = hardwareVersion;
    }

    public String getChargePointAvailabilityStatus()
    {
        return chargePointAvailabilityStatus;
    }

    public void setChargePointAvailabilityStatus( String chargePointAvailabilityStatus )
    {
        this.chargePointAvailabilityStatus = chargePointAvailabilityStatus;
    }

    public String getChargePointPowerStatus()
    {
        return chargePointPowerStatus;
    }

    public void setChargePointPowerStatus( String chargePointPowerStatus )
    {
        this.chargePointPowerStatus = chargePointPowerStatus;
    }

    @Override
    public void createBean( Object object )
    {
        ChargePoint chargePoint = (ChargePoint)object;
        setChargePointId( chargePoint.getChargePointId() );
        setReference(chargePoint.getReference());
        setLocationId(chargePoint.getLocationId());
        setPower(chargePoint.getPower());
        setType(chargePoint.getType());
        setProtocol(chargePoint.getProtocol());
        setFirmwareVersion(chargePoint.getFirmwareVersion());
        setHardwareVersion(chargePoint.getHardwareVersion());
        setLastUpdateDate(chargePoint.getLastUpdateDate());
        setLastUpdateTimeStamp(chargePoint.getLastUpdateTimeStamp());
        setUserName( chargePoint.getUserName() );
        setMachineUniqueRef(chargePoint.getMachineUniqueRef());
        setChargePointAvailabilityStatus(chargePoint.getChargePointAvailabilityStatus());
        setChargePointPowerStatus(chargePoint.getChargePointPowerStatus());

        //Load Special Display Attributes.
        setChargeLocationBean(loadChargeLocation(getLocationId()));

    }

    private LocationBean loadChargeLocation(int locationId)
    {
        LocationBean locationBean = new LocationBean();
        ChgResponse chgResponse = LocationLoader.loadSpecificLocationByLocationID(locationId);
        if (chgResponse.isSuccess())
        {
            ChargeLocation chargeLocation = (ChargeLocation)chgResponse.getReturnData();
            locationBean.createBean( chargeLocation );
            return locationBean;
        }
        return locationBean;
    }

    @Override
    public void decodeBeanToReal( Object object )
    {
        ChargePoint chargePoint = (ChargePoint)object;
        chargePoint.init();
        chargePoint.setChargePointId(getChargePointId());
        chargePoint.setReference(getReference());
        chargePoint.setLocationId(getLocationId());
        chargePoint.setPower(getPower());
        chargePoint.setType(getType());
        chargePoint.setProtocol(getProtocol());
        chargePoint.setFirmwareVersion(getFirmwareVersion());
        chargePoint.setHardwareVersion(getHardwareVersion());
        chargePoint.setLastUpdateDate(getLastUpdateDate());
        chargePoint.setLastUpdateTimeStamp(getLastUpdateTimeStamp());
        chargePoint.setUserName(getUserName());
        chargePoint.setMachineUniqueRef( getMachineUniqueRef()== null ? getReference() : getMachineUniqueRef() );
        chargePoint.setChargePointAvailabilityStatus( getChargePointAvailabilityStatus() );
        chargePoint.setChargePointPowerStatus( getChargePointPowerStatus() );
    }


}
