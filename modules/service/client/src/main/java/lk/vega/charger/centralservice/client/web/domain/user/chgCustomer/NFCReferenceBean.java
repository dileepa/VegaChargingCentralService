package lk.vega.charger.centralservice.client.web.domain.user.chgCustomer;

import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.core.NFCReference;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/22/15.
 * Time on 10:21 AM
 */
public class NFCReferenceBean extends DomainBeanImpl
{
    private int nfcNetworkMapID;
    private String reference;
    private int networkId;
    private boolean selected;
    private String userName;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    @Override
    public void createBean( Object object )
    {
        NFCReference nfcReference = (NFCReference)object;
        setUserName( nfcReference.getUserName() );
        setNfcNetworkMapID( nfcReference.getNfcNetworkMapID() );
        setReference( nfcReference.getReference() );
        setNetworkId( nfcReference.getNetworkId() );
        setSelected( false );
    }

    @Override
    public void decodeBeanToReal( Object object )
    {
        super.decodeBeanToReal( object );
    }

    public int getNfcNetworkMapID()
    {
        return nfcNetworkMapID;
    }

    public void setNfcNetworkMapID( int nfcNetworkMapID )
    {
        this.nfcNetworkMapID = nfcNetworkMapID;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference( String reference )
    {
        this.reference = reference;
    }

    public int getNetworkId()
    {
        return networkId;
    }

    public void setNetworkId( int networkId )
    {
        this.networkId = networkId;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected( boolean selected )
    {
        this.selected = selected;
    }
}
