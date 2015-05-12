package lk.vega.charger.centralservice.client.web.dataLoader.user;

import lk.vega.charger.centralservice.client.web.domain.user.ChgUserBean;
import org.wso2.carbon.um.ws.service.AddUser;

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

}
