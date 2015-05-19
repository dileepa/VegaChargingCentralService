package lk.vega.charger.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/19/15.
 * Time on 3:38 PM
 */
public class StringUtil
{

    public static String getCommaSeparatedStringFromStringList(List<String> list)
    {
        StringBuilder sb = new StringBuilder(  );
        for( String text : list )
        {
            sb.append( (sb.toString().length()) > 0 ? "," : "" );
            sb.append( text ) ;
        }

        return sb.toString();
    }


    public static void main( String[] args )
    {
        List<String> texts = new ArrayList<String>(  );
        texts.add( "HEEE" );
        texts.add( "oooo" );
        texts.add( "kkkk" );
        System.out.println(getCommaSeparatedStringFromStringList( texts ));
    }

}
