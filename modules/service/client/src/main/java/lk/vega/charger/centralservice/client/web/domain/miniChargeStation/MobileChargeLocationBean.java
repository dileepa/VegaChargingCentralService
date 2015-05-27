package lk.vega.charger.centralservice.client.web.domain.miniChargeStation;

import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/20/15.
 * Time on 5:45 PM
 */
public class MobileChargeLocationBean extends DomainBeanImpl
{

    private String chargerReference;
    private String LocationName;
    private String longitude;
    private String latitude;
    private String gpsLocation;
    private String workingStatus;

    public String getWorkingStatus()
    {
        return workingStatus;
    }

    public void setWorkingStatus( String workingStatus )
    {
        this.workingStatus = workingStatus;
    }

    public String getChargerReference()
    {
        return chargerReference;
    }

    public void setChargerReference( String chargerReference )
    {
        this.chargerReference = chargerReference;
    }

    public String getLocationName()
    {
        return LocationName;
    }

    public void setLocationName( String locationName )
    {
        LocationName = locationName;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude( String longitude )
    {
        this.longitude = longitude;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude( String latitude )
    {
        this.latitude = latitude;
    }

    public String getGpsLocation()
    {
        return gpsLocation;
    }

    public void setGpsLocation( String gpsLocation )
    {
        this.gpsLocation = gpsLocation;
    }

    @Override
    public void createBean( Object object )
    {
        ChargeStationBean chargeStationBean = (ChargeStationBean)object;
        setChargerReference( chargeStationBean.getReference() );
        setGpsLocation( chargeStationBean.getChargeLocationBean().getGpsLocation() );
        setLatitude( chargeStationBean.getChargeLocationBean().getLatitude() );
        setLongitude( chargeStationBean.getChargeLocationBean().getLongitude() );
        setLocationName( chargeStationBean.getChargeLocationBean().getName() );
        setWorkingStatus( chargeStationBean.getWorkingStatus() );
    }

    @Override
    public void decodeBeanToReal( Object object )
    {
        super.decodeBeanToReal( object );
    }
}
