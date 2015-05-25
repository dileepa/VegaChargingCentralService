package lk.vega.charger.centralservice.client.web.domain.user.chgCustomer;

import lk.vega.charger.centralservice.client.web.dataLoader.chargeNetwork.ChargeNetworkLoader;
import lk.vega.charger.centralservice.client.web.domain.chargeNetwork.ChargeNetworkBean;
import lk.vega.charger.centralservice.client.web.domain.user.ChgUserBean;
import lk.vega.charger.centralservice.client.web.validator.NFCReference;
import lk.vega.charger.core.ChargeNetwork;
import lk.vega.charger.core.ChgCustomerUser;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/22/15.
 * Time on 10:15 AM
 */
public class ChgCustomerBean extends ChgUserBean
{
    @NFCReference
    private String nfcRef;
    private int userCusMappingID;
    private List<NFCReferenceBean> nfcReferenceBeanList;
    private String createdBy;
    private List<String> networkIds;
    private List<ChargeNetworkBean> networks;
    private double customerPoints;

    public double getCustomerPoints()
    {
        return customerPoints;
    }

    public void setCustomerPoints( double customerPoints )
    {
        this.customerPoints = customerPoints;
    }

    public List<ChargeNetworkBean> getNetworks()
    {
        if( networks == null )
        {
            networks = new ArrayList<ChargeNetworkBean>();
        }
        return networks;
    }

    public void setNetworks( List<ChargeNetworkBean> networks )
    {
        this.networks = networks;
    }

    public List<NFCReferenceBean> getNfcReferenceBeanList()
    {
        if( nfcReferenceBeanList == null )
        {
            nfcReferenceBeanList = new ArrayList<NFCReferenceBean>();
        }
        return nfcReferenceBeanList;
    }

    public void setNfcReferenceBeanList( List<NFCReferenceBean> nfcReferenceBeanList )
    {
        this.nfcReferenceBeanList = nfcReferenceBeanList;
    }

    public List<String> getNetworkIds()
    {
        if( networkIds == null )
        {
            networkIds = new ArrayList<String>();
        }
        return networkIds;
    }

    public void setNetworkIds( List<String> chargeStationIds )
    {
        this.networkIds = chargeStationIds;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

    public String getNfcRef()
    {
        return nfcRef;
    }

    public void setNfcRef( String nfcRef )
    {
        this.nfcRef = nfcRef;
    }

    public int getUserCusMappingID()
    {
        return userCusMappingID;
    }

    public void setUserCusMappingID( int userCusMappingID )
    {
        this.userCusMappingID = userCusMappingID;
    }

    @Override
    public void createBean( Object object )
    {
        ChgCustomerUser chgCustomerUser = (ChgCustomerUser) object;
        setUserId( chgCustomerUser.getUserId() );
        setUserName( chgCustomerUser.getUserName() );
        setNfcRef( chgCustomerUser.getNfcRef() );
        setUserCusMappingID( chgCustomerUser.getUserCusMappingID() );
        setCreatedTimeStamp( chgCustomerUser.getCreatedTimeStamp() );
        setLastUpdateTimeStamp( chgCustomerUser.getLastUpdateTimeStamp() );
        setCreatedBy( chgCustomerUser.getCreatedBy() );
        setUserStatus( chgCustomerUser.getUserStatus() );
        setUserType( chgCustomerUser.getUserType() );
        setCustomerPoints( chgCustomerUser.getCustomerPoints() );

        //Set Special Values;
        setNfcReferenceBeanList( loadNFCReferences( chgCustomerUser.getNfcReferenceList() ) );
        if( !getNfcReferenceBeanList().isEmpty() )
        {
            setNetworkIds( loadNetworkIds( chgCustomerUser.getNfcReferenceList() ) );
            setNetworks( loadChargeNetworksBeanList( StringUtil.getCommaSeparatedStringFromStringList( getNetworkIds() ) ) );
        }

    }

    private List<ChargeNetworkBean> loadChargeNetworksBeanList( String commaSeparatedNetworkIds )
    {
        List allChargeNetworkBeanListUnderCustomer = new ArrayList<ChargeNetworkBean>();
        ChgResponse loadSavedNetworkStationsRes = ChargeNetworkLoader.loadSpecificNetworksByIds( commaSeparatedNetworkIds );
        if( loadSavedNetworkStationsRes.isSuccess() )
        {
            List<ChargeNetwork> savedChargeNetworkAndStationMappingList = (List) loadSavedNetworkStationsRes.getReturnData();
            for( ChargeNetwork chargeNetwork : savedChargeNetworkAndStationMappingList )
            {
                ChargeNetworkBean chargeNetworkBean = new ChargeNetworkBean();
                chargeNetworkBean.createBean( chargeNetwork );
                allChargeNetworkBeanListUnderCustomer.add( chargeNetworkBean );
            }
        }
        return allChargeNetworkBeanListUnderCustomer;

    }

    private List<String> loadNetworkIds( List<lk.vega.charger.core.NFCReference> nfcReferenceList )
    {
        List<String> networkIDS = new ArrayList<String>();
        for( lk.vega.charger.core.NFCReference nfcReference : nfcReferenceList )
        {
            networkIDS.add( String.valueOf( nfcReference.getNetworkId() ) );
        }
        return networkIDS;

    }

    private List<NFCReferenceBean> loadNFCReferences( List<lk.vega.charger.core.NFCReference> nfcReferenceList )
    {
        List<NFCReferenceBean> nfcReferenceBeans = new ArrayList<NFCReferenceBean>();
        for( lk.vega.charger.core.NFCReference nfcReference : nfcReferenceList )
        {
            NFCReferenceBean nfcReferenceBean = new NFCReferenceBean();
            nfcReferenceBean.createBean( nfcReference );
            nfcReferenceBeans.add( nfcReferenceBean );
        }
        return nfcReferenceBeans;
    }

    @Override
    public void decodeBeanToReal( Object object )
    {
        ChgCustomerUser chgCustomerUser = (ChgCustomerUser) object;
        chgCustomerUser.init();
        chgCustomerUser.setUserId( getUserId() );
        chgCustomerUser.setUserName( getUserName() );
        chgCustomerUser.setNfcRef( getNfcRef() );
        chgCustomerUser.setUserCusMappingID( getUserCusMappingID() );
        chgCustomerUser.setCreatedTimeStamp( getCreatedTimeStamp() );
        chgCustomerUser.setLastUpdateTimeStamp( getLastUpdateTimeStamp() );
        chgCustomerUser.setCreatedBy( getCreatedBy() );
        chgCustomerUser.setUserStatus( getUserStatus() );
        chgCustomerUser.setUserType( getUserType() );
        chgCustomerUser.setCustomerPoints( getCustomerPoints() );
    }
}
