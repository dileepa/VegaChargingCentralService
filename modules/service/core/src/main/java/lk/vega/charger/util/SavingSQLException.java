package lk.vega.charger.util;

import java.sql.SQLException;

/**
 * Created by dileepa on 3/19/15.
 */
public class SavingSQLException extends SQLException
{
    public SavingSQLException()
    {
        super();
    }

    public SavingSQLException( String reason )
    {
        super( reason );
    }

    public SavingSQLException( String reason, String sqlState )
    {
        super( reason, sqlState );
    }

    public SavingSQLException( String reason, String sqlState, int vendorCode )
    {
        super( reason, sqlState, vendorCode );
    }

    public SavingSQLException( SQLException se )
    {
        super( se.getMessage(), se.getSQLState(), se.getErrorCode() );
    }

}
