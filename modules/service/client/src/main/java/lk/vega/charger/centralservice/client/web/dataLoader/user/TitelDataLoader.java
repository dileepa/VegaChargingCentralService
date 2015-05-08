package lk.vega.charger.centralservice.client.web.dataLoader.user;

import lk.vega.charger.centralservice.client.web.domain.user.TitleBean;
import lk.vega.charger.util.ChgResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/8/15.
 * Time on 4:41 PM
 */
public class TitelDataLoader
{
    public static ChgResponse loadAllTitleProperties()
    {
        List<String> titleList = new ArrayList<String>(  );
        titleList.add( TitleBean.MR );
        titleList.add( TitleBean.MISS );
        titleList.add( TitleBean.DR );
        return new ChgResponse( ChgResponse.SUCCESS,"Load Charge Station Status Successfully",titleList  );
    }
}
