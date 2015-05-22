package lk.vega.charger.centralservice.client.web.dataLoader.user;

import lk.vega.charger.core.ChgUser;
import lk.vega.charger.util.ChgResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/22/15.
 * Time on 12:10 PM
 */
public class UserStatusDataLoader
{
    public static ChgResponse loadAllUserStatusProperties()
    {
        List<String> userStatusList = new ArrayList<String>(  );
        userStatusList.add( ChgUser.ACTIVE_USER );
        userStatusList.add( ChgUser.INACTIVE_USER );
        userStatusList.add( ChgUser.BLOCKED_USER );
        return new ChgResponse( ChgResponse.SUCCESS,"Load User Status Loaded Successfully",userStatusList  );
    }

}
