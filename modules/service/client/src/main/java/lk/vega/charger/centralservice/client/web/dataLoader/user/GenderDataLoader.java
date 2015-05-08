package lk.vega.charger.centralservice.client.web.dataLoader.user;

import lk.vega.charger.centralservice.client.web.domain.user.GenderBean;
import lk.vega.charger.util.ChgResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/8/15.
 * Time on 4:37 PM
 */
public class GenderDataLoader
{
    public static ChgResponse loadAllGenderProperties()
    {
        List<String> genderList = new ArrayList<String>(  );
        genderList.add( GenderBean.MALE );
        genderList.add( GenderBean.FEMALE );
        return new ChgResponse( ChgResponse.SUCCESS,"Load Charge Station Status Successfully",genderList  );
    }
}
