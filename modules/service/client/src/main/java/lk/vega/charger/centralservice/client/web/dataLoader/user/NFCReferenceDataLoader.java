package lk.vega.charger.centralservice.client.web.dataLoader.user;

import lk.vega.charger.core.NFCReference;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.connection.CHGConnectionPoolFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/22/15.
 * Time on 10:25 AM
 */
public class NFCReferenceDataLoader
{

    public static ChgResponse loadNetworksBySpecificNFC( String nfcReference )
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder(  );
        List nfcReferences  = new ArrayList<NFCReference>(  );
        sb.append( "SELECT * FROM NFC_NETWORK_MAP WHERE NFC_REF = ?" );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, nfcReference );
            rs = ps.executeQuery();
            while( rs.next() )
            {
                NFCReference nfcReferenceObject = new NFCReference();
                nfcReferenceObject.init();
                nfcReferenceObject.load( rs, con, 0 );
                nfcReferences.add( nfcReferenceObject );
            }
            return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", nfcReferences);

        }
        catch( SQLException e )
        {
            e.printStackTrace();
            return new ChgResponse( ChgResponse.ERROR, e.getMessage() );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            return new ChgResponse( ChgResponse.ERROR, e.getMessage());
        }
        finally
        {
            DBUtility.close( rs );
            DBUtility.close( ps );
            DBUtility.close( con );
        }
    }
}
