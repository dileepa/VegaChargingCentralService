package lk.vega.charger.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dileepa on 3/19/15.
 */
public class Savable
{
    /** 20000 */
    public static final int MODIFIED = 20000;
    /** 30000 */
    public static final int NEW = 30000;
    /** 40000 */
    public static final int DELETED = 40000;
    /** 50000 */
    public static final int UNCHANGED = 50000;

    public void save( Connection con ) throws SavingSQLException
    {
        throw new SavingSQLException("Needs Implementaion");
    }

    public void load(ResultSet rs, Connection con, int level) throws SQLException
    {
        throw new SavingSQLException("Needs Implementaion");
    }

    public int getStatus()
    {
        return -1;
    }

    public void setStatus(int i)
    {

    }

}
