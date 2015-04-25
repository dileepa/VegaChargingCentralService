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
    private String version;
    private ChgDate lastUpdateDate;
    private ChgTimeStamp lastUpdateTimeStamp;
    private int userId;
    private LocationBean chargeLocationBean;
    private String machineUniqueRef;

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

    public int getUserId()
    {
        return userId;
    }

    public void setUserId( int userId )
    {
        this.userId = userId;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
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
        setVersion(chargePoint.getVersion());
        setLastUpdateDate(chargePoint.getLastUpdateDate());
        setLastUpdateTimeStamp(chargePoint.getLastUpdateTimeStamp());
        setUserId(chargePoint.getUserId());
        setMachineUniqueRef(chargePoint.getMachineUniqueRef());

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
        chargePoint.setVersion(getVersion());
        chargePoint.setLastUpdateDate(getLastUpdateDate());
        chargePoint.setLastUpdateTimeStamp(getLastUpdateTimeStamp());
        chargePoint.setUserId(getUserId());
        chargePoint.setMachineUniqueRef( getMachineUniqueRef() );
    }


}
