package lk.vega.charger.centralservice.client.web.dataLoader.user;

import lk.vega.charger.centralservice.client.web.domain.user.ChgUserBean;
import lk.vega.charger.userManagement.UserHandler;
import lk.vega.charger.util.ChgResponse;
import org.wso2.carbon.um.ws.service.AddUser;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServicePortType;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceUserStoreException_Exception;
import org.wso2.carbon.um.ws.service.dao.xsd.ClaimDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/8/15.
 * Time on 3:24 PM
 */
public class ChgUserDataLoader
{


    public static AddUser createChargeUser( ChgUserBean chgUserBean )
    {
        AddUser newChargeOwner = new AddUser();
        newChargeOwner.setUserName( chgUserBean.getUserName() );
        newChargeOwner.setRequirePasswordChange( false );
        newChargeOwner.setCredential( chgUserBean.getPassword() );
        newChargeOwner.setProfileName( chgUserBean.getProfileName() );
        newChargeOwner.getRoleLists().add( chgUserBean.getUserRole() );
        newChargeOwner.getClaims().addAll( UserClaimAttributes.getClaimValuesForChgUser( chgUserBean ) );
        return newChargeOwner;
    }

    public static ChgResponse getUserListForSpecificRole (String roleName)
    {
        List<ChgUserBean> userBeans = new ArrayList<ChgUserBean>(  );
        ChgResponse chgResponse = new ChgResponse(  );
        chgResponse.setNo( ChgResponse.ERROR );
        try
        {

            chgResponse = UserHandler.connectToRemoteUserStoreManagerService();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        if( chgResponse.isSuccess() )
        {

            RemoteUserStoreManagerServicePortType remoteUserStoreManagerService = (RemoteUserStoreManagerServicePortType) chgResponse.getReturnData();
            try
            {
                List<String> userNames = remoteUserStoreManagerService.getUserListOfRole( roleName );
                for( String userName : userNames )
                {
                    ChgUserBean chgUserBean = new ChgUserBean();
                    chgUserBean.setSelected( false );
                    chgUserBean.setUserName( userName );
                    userBeans.add( chgUserBean );
                }
                chgResponse.setNo( ChgResponse.SUCCESS );
                chgResponse.setReturnData( userBeans );
            }
            catch( RemoteUserStoreManagerServiceUserStoreException_Exception e )
            {
                e.printStackTrace();
            }
        }
        return chgResponse;
    }


    public static ChgResponse loadUserProfileDetailsBySpecificUser (String username)
    {
        ChgResponse chgResponse = new ChgResponse(  );
        chgResponse.setNo( ChgResponse.ERROR );
        try
        {

            chgResponse = UserHandler.connectToRemoteUserStoreManagerService();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        if( chgResponse.isSuccess() )
        {

            RemoteUserStoreManagerServicePortType remoteUserStoreManagerService = (RemoteUserStoreManagerServicePortType) chgResponse.getReturnData();
            try
            {
                List<ClaimDTO> userClaims = remoteUserStoreManagerService.getUserClaimValues( username,"default" );
                chgResponse.setNo( ChgResponse.SUCCESS );
                chgResponse.setReturnData( userClaims );
            }
            catch( RemoteUserStoreManagerServiceUserStoreException_Exception e )
            {
                e.printStackTrace();
            }
        }

        return chgResponse;
    }

}
