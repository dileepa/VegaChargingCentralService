package lk.vega.charger.core;

import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.SavingSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/21/15.
 * Time on 2:56 PM
 */
public class ChgCustomerUser extends ChgUser
{

    private String nfcRef;
    private int userCusMappingID;
    private List<NFCReference> nfcReferenceList;

    public int getUserCusMappingID()
    {
        return userCusMappingID;
    }

    public void setUserCusMappingID( int userCusMappingID )
    {
        this.userCusMappingID = userCusMappingID;
    }

    public String getNfcRef()
    {
        return nfcRef;
    }

    public void setNfcRef( String nfcRef )
    {
        this.nfcRef = nfcRef;
    }

    public List<NFCReference> getNfcReferenceList()
    {
        if( nfcReferenceList == null )
        {
            nfcReferenceList = new ArrayList<NFCReference>();
        }
        return nfcReferenceList;
    }

    public void setNfcReferenceList( List<NFCReference> nfcReferenceList )
    {
        this.nfcReferenceList = nfcReferenceList;
    }


    @Override
    public void save( Connection con ) throws SavingSQLException
    {
        super.save( con );
        if( nfcReferenceList != null && nfcRef != null && !NFCReference.DUMMY_NFC_REF.equals( nfcRef ))
        {
            for( NFCReference nfcReference : nfcReferenceList )
            {
                if( this.status == Savable.NEW )
                {
                    nfcReference.setStatus( Savable.NEW );
                }
                nfcReference.setReference( this.nfcRef );
                nfcReference.save( con );
            }
        }

    }

    public void checkValidity() throws SavingSQLException
    {

    }

    protected void insert( Connection con ) throws SQLException
    {
        super.insert( con );
        StringBuilder sb = new StringBuilder( "INSERT INTO CHG_USER_CUSTOMER ( " );
        sb.append( "CUS_USERNAME, " );
        sb.append( "NFC_REF " );
        sb.append( ") VALUES(?,?)" );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.userName );
            ps.setString( ++count, this.nfcRef );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }

    protected void update( Connection con ) throws SQLException
    {
        super.update( con );
        StringBuilder sb = new StringBuilder( "UPDATE CHG_USER_CUSTOMER SET " );
        sb.append( "CUS_USERNAME = ?, " );
        sb.append( "NFC_REF = ? " );
        sb.append( "WHERE " );
        sb.append( "CHG_CUS_USER_MAPPING_ID = ? " );
        int count = 0;
        PreparedStatement ps = null;
        try
        {
            ps = con.prepareStatement( sb.toString() );
            ps.setString( ++count, this.userName );
            ps.setString( ++count, this.nfcRef );
            ps.setInt( ++count, this.userCusMappingID );
            ps.execute();
            ps.close();
        }
        finally
        {
            DBUtility.close( ps );
        }
    }

    protected void delete( Connection con ) throws SQLException
    {

    }

    @Override
    public void load( ResultSet rs, Connection con, int level ) throws SQLException
    {
        super.load( rs, con, level );

        this.status = Savable.UNCHANGED;
        this.userName = rs.getString( "CUS_USERNAME" );
        this.nfcRef = rs.getString( "NFC_REF" );
        this.userCusMappingID = rs.getInt( "CHG_CUS_USER_MAPPING_ID" );

        StringBuilder sb = new StringBuilder(  );
        sb.append( "SELECT * FROM NFC_NETWORK_MAP WHERE NFC_REF = ?" );

        PreparedStatement psNFCReferenceDetails = con.prepareStatement( sb.toString() );
        psNFCReferenceDetails.setString( 1, this.nfcRef );
        ResultSet rsNFCReferenceDetails = psNFCReferenceDetails.executeQuery();
        while( rsNFCReferenceDetails.next() )
        {
            NFCReference nfcReference = new NFCReference();
            nfcReference.init();
            nfcReference.load( rsNFCReferenceDetails, con, 0 );
            this.nfcReferenceList.add( nfcReference );
        }
    }

    public void init()
    {
        nfcRef = null;
        userName = null;
        nfcReferenceList = new ArrayList<NFCReference>(  );
        status = Savable.UNCHANGED;
    }
}
