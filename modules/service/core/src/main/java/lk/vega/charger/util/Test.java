package lk.vega.charger.util;

/**
 * Created by dileepa on 4/2/15.
 */
public class Test
{

    public static void main( String[] args )
    {
        ChgTimeStamp timeStamp = new ChgTimeStamp(  );
        System.out.println(timeStamp);
        int seconds = timeStamp.getSecond();
        timeStamp.setSecond( seconds + 10 );
        System.out.println(timeStamp);
    }
}
