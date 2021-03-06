package lk.vega.charger.centralservice.client.web.domain.location;

import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.core.ChargeLocation;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/24/15.
 * Time on 10:17 AM
 */
public class LocationBean extends DomainBeanImpl
{

    private int locationId;
    private String name;
    private String longitude;
    private String latitude;
    private String gpsLocation;
    private boolean selected;

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected( boolean selected )
    {
        this.selected = selected;
    }

    public int getLocationId()
    {
        return locationId;
    }

    public void setLocationId( int locationId )
    {
        this.locationId = locationId;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
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
        ChargeLocation chargeLocation = (ChargeLocation)object;
        setLocationId( chargeLocation.getLocationId() );
        setName( chargeLocation.getName() );
        setLatitude( chargeLocation.getLatitude() );
        setLongitude( chargeLocation.getLongitude() );
        setGpsLocation( chargeLocation.getGpsLocation() );
        setSelected( false );
    }

    @Override
    public void decodeBeanToReal( Object object )
    {
        ChargeLocation chargeLocation = (ChargeLocation)object;
        chargeLocation.init();
        chargeLocation.setLocationId( getLocationId() );
        chargeLocation.setName( getName() );
        chargeLocation.setLatitude( getLatitude() );
        chargeLocation.setLongitude( getLongitude() );
        chargeLocation.setGpsLocation( getGpsLocation() );
        setSelected( false );
    }

    @Override
    public String toString()
    {
        return name + " - {" + gpsLocation + "}";

    }
}
