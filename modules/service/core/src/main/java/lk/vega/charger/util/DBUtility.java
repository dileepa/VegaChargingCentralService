package lk.vega.charger.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by dileepa on 3/19/15.
 */
public class DBUtility
{

    public static void close( Connection con )
    {
        try
        {
            if( con != null && !con.isClosed() )
            {
                con.close();
            }
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }

    public static void close( ResultSet rs )
    {
        try
        {
            if( rs != null )
            {
                rs.close();
            }
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }

    public static void close( Statement statement )
    {
        try
        {
            if( statement != null )
            {
                statement.close();
            }
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }

    public static void rollBack( Connection con )
    {
        try
        {
            if( con != null )
            {
                con.rollback();
            }
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Closes the specified database objects. Accepts nulls.
     *
     * @param resultSet  ResultSet to close or null
     * @param statement  Statement to close or null
     * @param connection Connection to close or nulll
     */
    public static void close( ResultSet resultSet, Statement statement, Connection connection )
    {
        if( resultSet != null )
        {
            close( resultSet );
        }
        if( statement != null )
        {
            close( statement );
        }
        if( connection != null )
        {
            close( connection );
        }
    }

}
