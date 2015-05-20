package lk.vega.charger.centralservice.client.web.domain.chargeNetwork;

import lk.vega.charger.centralservice.client.web.dataLoader.chargeNetwork.ChargeNetworkLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;
import lk.vega.charger.core.ChargeNetwork;
import lk.vega.charger.core.ChargeNetworkAndStationMapping;
import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/15/15.
 * Time on 3:06 PM
 */
public class ChargeNetworkBean extends DomainBeanImpl
{
    private int networkId;
    private String reference;
    private String networkOwnerUserName;
    private double chargeAmount;
    private double chargeAmountForOtherNetwork;
    private double membershipFee;
    private double annualFee;
    private double maxChargeTime;
    private List<ChargeStationBean> chargeStationBeanList;
    private List<String> chargeStationIds;

    public List<ChargeStationBean> getChargeStationBeanList()
    {
        return chargeStationBeanList;
    }

    public void setChargeStationBeanList( List<ChargeStationBean> chargeStationBeanList )
    {
        this.chargeStationBeanList = chargeStationBeanList;
    }

    public List<String> getChargeStationIds()
    {
        return chargeStationIds;
    }

    public void setChargeStationIds( List<String> chargeStationIds )
    {
        this.chargeStationIds = chargeStationIds;
    }

    public int getNetworkId()
    {
        return networkId;
    }

    public void setNetworkId( int networkId )
    {
        this.networkId = networkId;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference( String reference )
    {
        this.reference = reference;
    }

    public String getNetworkOwnerUserName()
    {
        return networkOwnerUserName;
    }

    public void setNetworkOwnerUserName( String networkOwnerUserName )
    {
        this.networkOwnerUserName = networkOwnerUserName;
    }


    public double getChargeAmount()
    {
        return chargeAmount;
    }

    public void setChargeAmount( double chargeAmount )
    {
        this.chargeAmount = chargeAmount;
    }

    public double getChargeAmountForOtherNetwork()
    {
        return chargeAmountForOtherNetwork;
    }

    public void setChargeAmountForOtherNetwork( double chargeAmountForOtherNetwork )
    {
        this.chargeAmountForOtherNetwork = chargeAmountForOtherNetwork;
    }

    public double getMembershipFee()
    {
        return membershipFee;
    }

    public void setMembershipFee( double membershipFee )
    {
        this.membershipFee = membershipFee;
    }

    public double getAnnualFee()
    {
        return annualFee;
    }

    public void setAnnualFee( double annualFee )
    {
        this.annualFee = annualFee;
    }

    public double getMaxChargeTime()
    {
        return maxChargeTime;
    }

    public void setMaxChargeTime( double maxChargeTime )
    {
        this.maxChargeTime = maxChargeTime;
    }

    @Override
    public void createBean( Object object )
    {
        ChargeNetwork chargeNetwork = (ChargeNetwork)object;

        setNetworkId( chargeNetwork.getNetworkId() );
        setReference( chargeNetwork.getReference() );
        setNetworkOwnerUserName( chargeNetwork.getNetworkOwnerUserName() );
        setChargeAmount( chargeNetwork.getChargeAmount() );
        setChargeAmountForOtherNetwork( chargeNetwork.getChargeAmountForOtherNetwork() );
        setMembershipFee( chargeNetwork.getMembershipFee() );
        setAnnualFee( chargeNetwork.getAnnualFee() );
        setMaxChargeTime( chargeNetwork.getMaxChargeTime() );

        //Load Special Display Attributes.
        setChargeStationBeanList( loadChargeStationsBeanList(chargeNetwork.getNetworkId()) );
        setChargeStationIds( loadChgPointIdsList( getChargeStationBeanList() ) );
    }

    private List<String> loadChgPointIdsList( List<ChargeStationBean> chargeStationBeanList )
    {
        List<String> chargeIds = new ArrayList<String>(  );
        for (ChargeStationBean chargeStationBean : chargeStationBeanList)
        {
            chargeIds.add( String.valueOf( chargeStationBean.getChargePointId() ) );
        }
        return chargeIds;
    }

    private List<ChargeStationBean> loadChargeStationsBeanList( int networkId )
    {
        List allChargePointBeanListUnderNetwork = new ArrayList<ChargeStationBean>();
        ChgResponse loadSavedNetworkStationsRes = ChargeNetworkLoader.loadNetworkSpecificStationIds( networkId );
        if( loadSavedNetworkStationsRes.isSuccess() )
        {
            List<ChargeNetworkAndStationMapping> savedChargeNetworkAndStationMappingList = (List) loadSavedNetworkStationsRes.getReturnData();
            List<String> chargeStationIds = new ArrayList<String>();
            for( ChargeNetworkAndStationMapping chargeNetworkAndStationMapping : savedChargeNetworkAndStationMappingList )
            {
                chargeStationIds.add( String.valueOf( chargeNetworkAndStationMapping.getStationId() ) );
            }
            if( !chargeStationIds.isEmpty() )
            {
                String commaSeparatedIds = StringUtil.getCommaSeparatedStringFromStringList( chargeStationIds );
                ChgResponse chargeStationDaResponse = ChargeStationLoader.loadSpecificStationsByIds( commaSeparatedIds );
                if( chargeStationDaResponse.isSuccess() )
                {
                    List<ChargePoint> loadChargePoints = (List) chargeStationDaResponse.getReturnData();
                    allChargePointBeanListUnderNetwork = ChargeStationBean.getBeanList( loadChargePoints, DomainBeanImpl.CHARGE_STATION_BEAN_ID );
                }
            }
        }

        return allChargePointBeanListUnderNetwork;

    }

    @Override
    public void decodeBeanToReal( Object object )
    {
        ChargeNetwork chargeNetwork = (ChargeNetwork)object;
        chargeNetwork.init();
        chargeNetwork.setNetworkId( getNetworkId() );
        chargeNetwork.setReference( getReference() );
        chargeNetwork.setNetworkOwnerUserName( getNetworkOwnerUserName() );
        chargeNetwork.setChargeAmount( getChargeAmount() );
        chargeNetwork.setChargeAmountForOtherNetwork( getChargeAmountForOtherNetwork() );
        chargeNetwork.setMembershipFee( getMembershipFee() );
        chargeNetwork.setAnnualFee( getAnnualFee() );
        chargeNetwork.setMaxChargeTime( getMaxChargeTime() );
    }

    public List<ChargeNetworkAndStationMapping> createNetworkAndStationMappingList()
    {
        List<ChargeNetworkAndStationMapping> chargeNetworkAndStationMappingList = new ArrayList<ChargeNetworkAndStationMapping>(  );

        for( String chargeStationID : this.getChargeStationIds() )
        {
            ChargeNetworkAndStationMapping chargeNetworkAndStationMapping = new ChargeNetworkAndStationMapping();
            chargeNetworkAndStationMapping.init();
            chargeNetworkAndStationMapping.setNetworkId( this.getNetworkId() );
            chargeNetworkAndStationMapping.setStationId( Integer.parseInt( chargeStationID ) );
            chargeNetworkAndStationMapping.setStatus( Savable.NEW );
            chargeNetworkAndStationMappingList.add( chargeNetworkAndStationMapping );
        }

        return chargeNetworkAndStationMappingList;
    }
}
