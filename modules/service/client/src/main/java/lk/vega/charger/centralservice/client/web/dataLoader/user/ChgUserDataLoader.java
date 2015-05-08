package lk.vega.charger.centralservice.client.web.dataLoader.user;

import lk.vega.charger.centralservice.client.web.domain.user.ChgUser;
import org.wso2.carbon.um.ws.service.AddUser;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/8/15.
 * Time on 3:24 PM
 */
public class ChgUserDataLoader
{


    public static AddUser createChargeUser( ChgUser chgUser )
    {
        AddUser newChargeOwner = new AddUser();
        newChargeOwner.setUserName( chgUser.getUserName() );
        newChargeOwner.setRequirePasswordChange( false );
        newChargeOwner.setCredential( chgUser.getPassword() );
        newChargeOwner.setProfileName( chgUser.getProfileName() );
        newChargeOwner.getRoleLists().add( chgUser.getUserRole() );
        newChargeOwner.getClaims().addAll( UserClaimAttributes.getClaimValuesForChgUser( chgUser ) );
        return newChargeOwner;
    }

}
