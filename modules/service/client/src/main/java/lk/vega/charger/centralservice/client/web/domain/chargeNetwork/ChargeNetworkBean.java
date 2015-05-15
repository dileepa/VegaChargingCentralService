package lk.vega.charger.centralservice.client.web.domain.chargeNetwork;

import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.core.ChargeNetwork;

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
    private String chargingStationIds;
    private double chargeAmount;
    private double chargeAmountForOtherNetwork;
    private double membershipFee;
    private double annualFee;
    private double maxChargeTime;

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

    public String getChargingStationIds()
    {
        return chargingStationIds;
    }

    public void setChargingStationIds( String chargingStationIds )
    {
        this.chargingStationIds = chargingStationIds;
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
}
