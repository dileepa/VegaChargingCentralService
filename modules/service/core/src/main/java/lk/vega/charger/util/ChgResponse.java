package lk.vega.charger.util;

/**
 * Created by dileepa on 3/20/15.
 */
public class ChgResponse<T>
{
    private long no;
    private String msg;
    private T returnData;
    private String errorCode;

    /** 1 */
    public static final int SUCCESS = 1;
    /** -1 */
    public static final int ERROR = -1;
    /** 0 */
    public static final int WARNING = 0;
    /** -2 */


    public ChgResponse()
    {
    }

    public ChgResponse(long no, String msg)
    {
        this.no = no;
        this.msg = msg;
    }

    public ChgResponse(String msg, long no)
    {
        this.msg = msg;
        this.no = no;
    }

    public ChgResponse(String msg, long no, String errorCode)
    {
        this.msg = msg;
        this.no = no;
        this.errorCode = errorCode;
    }

    public ChgResponse(long no, String msg, T returnData)
    {
        this.no = no;
        this.msg = msg;
        this.returnData = returnData;
    }

    public ChgResponse(long no, String msg, T returnData,  String errorCode)
    {
        this.no = no;
        this.msg = msg;
        this.returnData = returnData;
        this.errorCode = errorCode;
    }

    public boolean isSuccess()
    {
        return this.no == ChgResponse.SUCCESS;
    }

    public boolean isError()
    {
        return this.no == ChgResponse.ERROR;
    }

    public boolean isWarning()
    {
        return this.no == ChgResponse.WARNING;
    }

    public String getErrorStatus()
    {
        if( no == ChgResponse.SUCCESS )
        {
            return "SUCCESS";
        }
        if( no == ChgResponse.ERROR )
        {
            return "ERROR";
        }
        if( no == ChgResponse.WARNING )
        {
            return "WARNING";
        }
        return "" + no;
    }
}
