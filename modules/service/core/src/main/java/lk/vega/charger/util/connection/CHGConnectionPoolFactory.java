package lk.vega.charger.util.connection;

import java.sql.SQLException;

/**
 * Created by dileepa on 3/19/15.
 */
public class CHGConnectionPoolFactory
{
    public static final String MYSQL = "MYSQL";


    public static ConnectionPool getCGConnectionPool( String database ) throws SQLException
    {
        return getCGConnectionPool( database, 0 );
    }

    public static ConnectionPool getCGConnectionPool( String database, int index ) throws SQLException
    {
        if( database.equals( MYSQL ) )
        {
            return MySQLConnectionPoolImpl.getInstance( index );
        }
        else
        {
            throw new SQLException( "Unsupported Connection Pool Requested" );
        }

    }

}
