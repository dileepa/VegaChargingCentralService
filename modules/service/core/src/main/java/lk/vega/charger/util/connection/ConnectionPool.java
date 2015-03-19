package lk.vega.charger.util.connection;

import java.sql.Connection;

/**
 * Created by dileepa on 3/19/15.
 */
public interface ConnectionPool
{
    public Connection getConnection() throws java.sql.SQLException;
}
