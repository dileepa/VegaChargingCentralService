package lk.vega.charger.centralservice.client.web.domain.location;

import lk.vega.charger.centralservice.client.web.domain.DomainBean;
import lk.vega.charger.core.ChargeLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/24/15.
 * Time on 10:17 AM
 */
public class LocationBean implements DomainBean
{

    private int locationId;
    private String name;
    private String longitude;
    private String latitude;
    private String gpsLocation;

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
    }

    public static List getBeanList( List objectList )
    {
        List<LocationBean> locationBeanList = new ArrayList<LocationBean>();
        for( Object o : objectList )
        {
            LocationBean locationBean = new LocationBean();
            locationBean.createBean( (ChargeLocation)o );
            locationBeanList.add( locationBean );
        }
        return locationBeanList;
    }
}
