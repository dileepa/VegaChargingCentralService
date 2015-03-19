package lk.vega.charger.util.connection;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: dileepa
 * Date: 3/17/15
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class MySQLConnectionPoolImpl implements ConnectionPool
{

    private static MySQLConnectionPoolImpl[] pool = new MySQLConnectionPoolImpl[5];
    DataSource datasource;

    static synchronized MySQLConnectionPoolImpl getInstance()
    {
        return getInstance(0);
    }

    MySQLConnectionPoolImpl(Properties p) throws SQLException
    {
        try
        {
            datasource = DBConnectionPool.setupDataSource("com.mysql.jdbc.Driver",p.getProperty("url").trim()+"?user="+p.getProperty("user")+"&password="+p.getProperty("password")+"&autoReconnect=true", p);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
        //        datasource = new MysqlConnectionPoolDataSource();
        //        ( ( MysqlConnectionPoolDataSource) datasource).setURL();
        //
        //        /* Set User name */
        //        ( ( MysqlConnectionPoolDataSource) datasource).setUser(p.getProperty("user"));
        //
        //        /* Set Password */
        //        ( ( MysqlConnectionPoolDataSource) datasource).setPassword(p.getProperty("password"));
        //
        //        ( ( MysqlConnectionPoolDataSource) datasource).setConnectTimeout(Integer.parseInt(p.getProperty("ConnectionWaitTimeout")));
    }

    static synchronized MySQLConnectionPoolImpl getInstance(int index)
    {
        if (pool[index] == null)
        {
            try
            {
                String prefix = "mysql_";
                if (index != 0)
                {
                    prefix = prefix + index + "_";
                }

                String confFile;

                if ( System.getProperty("vega.config.path") != null )
                {
                    confFile = System.getProperty("vega.config.path");
                }
                else
                {
                    System.out.println("Cannot Find DB configuration File");
                    return null;
                }
                //                String confFile="g:\\server.conf";
                System.out.println("CONFIG : " + confFile);
                Properties dbProperites = new Properties();
                dbProperites.load(new FileInputStream(confFile));

                Properties properties = new Properties();

                System.out.println("_________________________________________________________________________");
                System.out.println("|                                                                       |");
                System.out.println("|                                                                       |");
                System.out.println("|                  NEW CONNECTION POOL CREATED - VEGA                   |");
                System.out.println("|                         CACHED                                        |");
                System.out.println("|                                                                       |");
                System.out.println("_________________________________________________________________________");


                if (dbProperites.getProperty(prefix + "url") != null)
                {
                    properties.put("url", dbProperites.getProperty(prefix + "url"));
                }

                if (dbProperites.getProperty(prefix + "user") != null)
                {
                    properties.put("user", dbProperites.getProperty(prefix + "user"));
                }

                if (dbProperites.getProperty(prefix + "password") != null)
                {
                    properties.put("password", dbProperites.getProperty( prefix + "password" ));
                }
                if ( dbProperites.getProperty( prefix + "MaxLimit" ) != null )
                {
                    properties.put( "MaxLimit", dbProperites.getProperty( prefix + "MaxLimit" ) );
                }

                if (dbProperites.getProperty(prefix + "ConnectionWaitTimeout") != null)
                {
                    properties.put("ConnectionWaitTimeout", dbProperites.getProperty(prefix + "ConnectionWaitTimeout"));
                }


                pool[index] = new MySQLConnectionPoolImpl(properties);

                Enumeration e = dbProperites.keys();
                while (e.hasMoreElements())
                {
                    String s = (String) e.nextElement();
                    System.out.println(" [" + s + "] - " + dbProperites.get(s));
                }

                if (pool == null)
                {
                    System.out.println("pool created is null");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return pool[index];
    }


    @Override
    public Connection getConnection() throws SQLException
    {
        Connection con = datasource.getConnection();

        if (con == null)
        {
            throw new SQLException("Cannot Get Connection");
        }
        else
        {
            return con;
        }

    }
}
