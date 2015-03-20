package lk.vega.charger.util;

/**
 * Created by dileepa on 3/20/15.
 */
public class VegaError<T>
{
    private long no;
    private String msg;
    private T returnData;

    /** 1 */
    public static final int SUCCESS = 1;
    /** -1 */
    public static final int ERROR = -1;
    /** 0 */
    public static final int WARNING = 0;
    /** -2 */

    //TODO need to add error codes here.

    public  VegaError()
    {
    }

    public VegaError( long no, String msg )
    {
        this.no = no;
        this.msg = msg;
    }

    public VegaError( String msg, long no )
    {
        this.msg = msg;
        this.no = no;
    }

    public VegaError( long no, String msg, T returnData )
    {
        this.no = no;
        this.msg = msg;
        this.returnData = returnData;
    }

    public boolean isSuccess()
    {
        return this.no == VegaError.SUCCESS;
    }

    public boolean isError()
    {
        return this.no == VegaError.ERROR;
    }

    public boolean isWarning()
    {
        return this.no == VegaError.WARNING;
    }

    public String getErrorStatus()
    {
        if( no == VegaError.SUCCESS )
        {
            return "SUCCESS";
        }
        if( no == VegaError.ERROR )
        {
            return "ERROR";
        }
        if( no == VegaError.WARNING )
        {
            return "WARNING";
        }
        return "" + no;
    }
}
