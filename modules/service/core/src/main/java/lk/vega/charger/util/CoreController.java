package lk.vega.charger.util;

import lk.vega.charger.util.connection.CHGConnectionPoolFactory;
import lk.vega.charger.util.connection.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by dileepa on 3/20/15.
 */
public class CoreController
{
    public static ConnectionPool pool = null;

    //TODO call when service init.

    public void init()
    {
        try
        {
            pool = CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }


    public VegaError save(  Object obj )
    {
        VegaError err = new VegaError( VegaError.SUCCESS, "Sucessfully Saved....." );

        try
        {
            Connection con = pool.getConnection();
            try
            {
                con.setAutoCommit( false );
                Savable savObj;
                if( obj instanceof Savable )
                {
                    savObj = (Savable) obj;
                    int status = savObj.getStatus();
                    savObj.save( con );

                }
                else
                {
                    err = new VegaError( VegaError.ERROR, "Unknown Object Type '" + obj.getClass().getName() );
                }

                con.commit();
            }
            catch( SavingSQLException se )
            {
                try
                {
                    con.rollback();
                }
                catch( SQLException e )
                {
                    e.printStackTrace();
                }
                err = new VegaError( VegaError.ERROR,se.getMessage());

            }
            catch( Exception ne )
            {
                try
                {
                    con.rollback();
                }
                catch( SQLException e )
                {
                    e.printStackTrace();
                }
                err = new VegaError( VegaError.ERROR, ne.getMessage() );
            }
            finally
            {
                DBUtility.close( con );
            }
        }
        catch( SQLException se )
        {
            err = new VegaError( VegaError.ERROR, "ERROR: in connecting to database!" );
        }
        return err;
    }



}
