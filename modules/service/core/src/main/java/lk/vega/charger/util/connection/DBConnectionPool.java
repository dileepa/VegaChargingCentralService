package lk.vega.charger.util.connection;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.util.Properties;

/**

 /**
 * Created with IntelliJ IDEA.
 * User: dileepa
 * Date: 3/17/15
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBConnectionPool
{
    public static DataSource setupDataSource(String driver,String connectURI, Properties p) throws ClassNotFoundException
    {
        Class.forName(driver);

        //
        // First, we'll need a ObjectPool that serves as the
        // actual pool of connections.
        //
        // We'll use a GenericObjectPool instance, although
        // any ObjectPool implementation will suffice.
        //
        ObjectPool connectionPool = new GenericObjectPool( null );
        if ( connectionPool != null )
        {
            if ( p.getProperty( "MaxLimit" ) != null )
            {
                try
                {
                    ( ( GenericObjectPool ) connectionPool ).setMaxActive( Integer.parseInt( p.getProperty( "MaxLimit" ) ) );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        }

        //
        // Next, we'll create a ConnectionFactory that the
        // pool will use to create Connections.
        // We'll use the DriverManagerConnectionFactory,
        // using the connect string passed in the command line
        // arguments.
        //
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, null);

        //
        // Now we'll create the PoolableConnectionFactory, which wraps
        // the "real" Connections created by the ConnectionFactory with
        // the classes that implement the pooling functionality.
        //
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);

        // Finally, we create the PoolingDriver itself,
        // passing in the object pool we created.
        //
        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

        return dataSource;
    }

}
