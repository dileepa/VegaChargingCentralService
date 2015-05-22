package lk.vega.charger.centralservice.client.web.domain;

import lk.vega.charger.centralservice.client.web.domain.chargeNetwork.ChargeNetworkBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationAvailabilityStatusBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationPowerStatusBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationProtocolBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationTypeBean;
import lk.vega.charger.centralservice.client.web.domain.location.LocationBean;
import lk.vega.charger.centralservice.client.web.domain.user.GenderBean;
import lk.vega.charger.centralservice.client.web.domain.user.TitleBean;
import lk.vega.charger.centralservice.client.web.domain.user.UserStatusBean;
import lk.vega.charger.centralservice.client.web.domain.user.chgCustomer.ChgCustomerBean;
import lk.vega.charger.centralservice.client.web.domain.user.chgCustomer.NFCReferenceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/24/15.
 * Time on 1:28 PM
 */
public class DomainBeanImpl implements DomainBean
{
    /**
     *
     * This is the Abstract Class for create bean object.
     * in Child class must contain
     * override below methods
     * getters and setters
     */

    public static final int LOCATION_BEAN_ID = 1;
    public static final int CHARGE_STATION_BEAN_ID = 2;
    public static final int CHARGE_STATION_AVAILABILITY_STATUS_BEAN_ID = 3;
    public static final int USER_GENDER_BEAN_ID = 4;
    public static final int USER_TITLE_BEAN_ID = 5;
    public static final int CHARGE_STATION_POWER_STATUS_BEAN_ID = 6;
    public static final int CHARGE_STATION_PROTOCOL_BEAN_ID = 7;
    public static final int CHARGE_STATION_TYPE_BEAN_ID = 8;
    public static final int CHARGE_NETWORK_BEAN_ID = 9;
    public static final int USER_CUSTOMER_BEAN_ID = 10;
    public static final int NFC_REFERENCE_BEAN_ID = 11;
    public static final int USER_STATUS_BEAN_ID = 12;


    @Override
    public void createBean( Object object )
    {

    }

    @Override
    public void decodeBeanToReal( Object object )
    {

    }

    public static List<DomainBean> getBeanList( List objectList, int identifier )
    {
        Object beanObject = null;
        switch( identifier )
        {
            case 1:
                beanObject = new LocationBean();
                break;
            case 2:
                beanObject = new ChargeStationBean();
                break;
            case 3:
                beanObject = new ChargeStationAvailabilityStatusBean();
                break;
            case 4:
                beanObject = new GenderBean();
                break;
            case 5:
                beanObject = new TitleBean();
                break;
            case 6:
                beanObject = new ChargeStationPowerStatusBean();
                break;
            case 7:
                beanObject = new ChargeStationProtocolBean();
                break;
            case 8:
                beanObject = new ChargeStationTypeBean();
                break;
            case 9:
                beanObject = new ChargeNetworkBean();
                break;
            case 10:
                beanObject = new ChgCustomerBean();
                break;
            case 11:
                beanObject = new NFCReferenceBean();
                break;
            case 12:
                beanObject = new UserStatusBean();
                break;
            default:
                break;
        }
        List<DomainBean> locationBeanList = new ArrayList<DomainBean>();
        Class dynamicClass = beanObject.getClass();
        for( Object o : objectList )
        {
            try
            {
                Object bean = dynamicClass.newInstance();
                DomainBean domainBean = (DomainBean)bean;
                domainBean.createBean( o );
                locationBeanList.add( domainBean );
            }
            catch( InstantiationException e )
            {
                e.printStackTrace();
            }
            catch( IllegalAccessException e )
            {
                e.printStackTrace();
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
        return locationBeanList;
    }
}
