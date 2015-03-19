package lk.vega.charger.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * User: ruwan - copy from codegen
 * VegaDate: Dec 26, 2005
 * Time: 4:16:14 PM
 * Desc: If you set date by each field separately, follow the order year >> month >> date.
 *  Other wise you may get different date as output.
 */

public class VegaTimestamp implements Comparable, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = -3553535558207438349L;
    private Calendar cal;

    public VegaTimestamp()
    {
        this.cal = Calendar.getInstance();
    }

    public VegaTimestamp( long millis )
    {
        this();
        this.cal.setTimeInMillis( millis );
    }


    public VegaTimestamp( VegaDate VegaDate, VegaTime VegaTime )
    {
        this( VegaDate.getYear(), VegaDate.getMonth(), VegaDate.getDate(), VegaTime.getHour(), VegaTime.getMinute(), 0, 0 );
    }

    public VegaTimestamp( java.sql.Timestamp timestamp )
    {
        this();
        cal.setTimeInMillis( timestamp.getTime() );
    }


    public VegaTimestamp( java.util.Date date )
    {
        this();
        cal.setTimeInMillis( date.getTime() );
    }

    public VegaTimestamp( java.sql.Date date )
    {
        this();
        cal.setTimeInMillis( date.getTime() );
    }

    public VegaTimestamp( Calendar cal )
    {
        this.cal = cal;
    }

    /**
     * @param year  // offest from 1900 (as in java)
     * @param month // 0 - 11, as in java
     * @param date
     */
    public VegaTimestamp( int year, int month, int date, int hour, int minute, int second, int millisecond )
    {
        this();
        cal.set( Calendar.YEAR, year );
        cal.set( Calendar.MONTH, month );
        cal.set( Calendar.DATE, date );
        cal.set( Calendar.HOUR_OF_DAY, hour );
        cal.set( Calendar.MINUTE, minute );
        cal.set( Calendar.SECOND, second );
        cal.set( Calendar.MILLISECOND, millisecond );
    }

    public int _get( int field )
    {
        return cal.get( field );
    }

    public void _set( int field, int value )
    {
        cal.set( field, value );
    }

    public void add( int field, int value )
    {
        cal.add( field, value );
    }

    public boolean equals( VegaTimestamp VegaTimestamp )
    {
        if ( ( VegaTimestamp._getCalendar().get( Calendar.YEAR ) == cal.get( Calendar.YEAR ) ) &&
                ( VegaTimestamp._getCalendar().get( Calendar.MONTH ) == cal.get( Calendar.MONTH ) ) &&
                ( VegaTimestamp._getCalendar().get( Calendar.DATE ) == cal.get( Calendar.DATE ) ) &&
                ( VegaTimestamp._getCalendar().get( Calendar.HOUR_OF_DAY ) == cal.get( Calendar.HOUR_OF_DAY ) ) &&
                ( VegaTimestamp._getCalendar().get( Calendar.MINUTE ) == cal.get( Calendar.MINUTE ) ) &&
                ( VegaTimestamp._getCalendar().get( Calendar.SECOND ) == cal.get( Calendar.SECOND ) ) &&
                ( VegaTimestamp._getCalendar().get( Calendar.MILLISECOND ) == cal.get( Calendar.MILLISECOND ) ) )
        {
            return true;
        }

        return false;
    }

    public boolean dateEquals( VegaDate VegaDate )
    {
        if ( ( VegaDate.getYear() == cal.get( Calendar.YEAR ) ) &&
                ( VegaDate.getMonth() == cal.get( Calendar.MONTH ) ) &&
                ( VegaDate.getDate() == cal.get( Calendar.DATE ) ) )
        {
            return true;
        }

        return false;
    }

    public boolean dateEquals( VegaTimestamp VegaDate )
    {
        if ( ( VegaDate.getYear() == cal.get( Calendar.YEAR ) ) &&
                ( VegaDate.getMonth() == cal.get( Calendar.MONTH ) ) &&
                ( VegaDate.getDate() == cal.get( Calendar.DATE ) ) )
        {
            return true;
        }

        return false;
    }

    public boolean timeEquals( VegaTimestamp VegaTimestamp )
    {
        if ( ( VegaTimestamp._getCalendar().get( Calendar.YEAR ) == cal.get( Calendar.YEAR ) ) &&
                ( VegaTimestamp._getCalendar().get( Calendar.MONTH ) == cal.get( Calendar.MONTH ) ) &&
                ( VegaTimestamp._getCalendar().get( Calendar.DATE ) == cal.get( Calendar.DATE ) ) &&
                ( VegaTimestamp._getCalendar().get( Calendar.HOUR_OF_DAY ) == cal.get( Calendar.HOUR_OF_DAY ) ) &&
                ( VegaTimestamp._getCalendar().get( Calendar.MINUTE ) == cal.get( Calendar.MINUTE ) ) )
        {
            return true;
        }

        return false;
    }

    public int compareTo( Object o )
    {
        //        if ( o instanceof VegaTimestamp )
        //        {
        //            if ( after( ( VegaTimestamp ) o ) )
        //            {
        //                return 1;
        //            }
        //            else
        //            {
        //                return 0;
        //            }
        //        }
        if ( equals( o ) )
        {
            return 0;
        }
        else if ( after( ( VegaTimestamp ) o ) )
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    public boolean after( VegaDate date )
    {
        // if not the same date check by the calander after method
        //        return !equals( date ) && cal.after( date._getCalendar() );
        return !equals( date ) && cal.getTimeInMillis() > date._getTimeInMillis();
    }

    public boolean before( VegaDate date )
    {
        // if not the same date check by the calander before method
        return !equals( date ) && cal.getTimeInMillis() < date._getTimeInMillis();
    }

    public boolean after( VegaTimestamp time )
    {
        // if not the same date check by the calander after method
        //        return !equals( date ) && cal.after( date._getCalendar() );
        return !equals( time ) && cal.getTimeInMillis() > time._getCalendar().getTimeInMillis();
    }

    public boolean before( VegaTimestamp time )
    {
        // if not the same date check by the calander before method
        return !equals( time ) && cal.getTimeInMillis() < time._getCalendar().getTimeInMillis();
    }

    public Object clone()
    {
        return new VegaTimestamp( getYear(), getMonth(), getDate(), getHour(), getMinute(), getSecond(), getMilisecond() );
    }

    public Calendar _getCalendar()
    {
        return cal;
    }

    public void _setCalendar( Calendar cal )
    {
        this.cal = cal;
    }

    public java.sql.Timestamp _getSQLTimestamp()
    {
        return new java.sql.Timestamp( cal.getTimeInMillis() );
    }

    public void _setSQLTimestamp( java.sql.Timestamp timestamp )
    {
        cal.setTimeInMillis( timestamp.getTime() );
    }

    public int getYear()
    {
        return cal.get( Calendar.YEAR );
    }

    public void setYear( int year )
    {
        cal.set( Calendar.YEAR, year );
    }

    public int getMonth()
    {
        return cal.get( Calendar.MONTH );
    }

    public void setMonth( int month )
    {
        cal.set( Calendar.MONTH, month );
    }

    public int getDate()
    {
        return cal.get( Calendar.DATE );
    }

    public void setDate( int date )
    {
        cal.set( Calendar.DATE, date );
    }

    public int getHour()
    {
        return cal.get( Calendar.HOUR_OF_DAY );
    }

    public void setHour( int hour )
    {
        cal.set( Calendar.HOUR_OF_DAY, hour );
    }

    public int getMinute()
    {
        return cal.get( Calendar.MINUTE );
    }

    public void setMinute( int minute )
    {
        cal.set( Calendar.MINUTE, minute );
    }

    public int getSecond()
    {
        return cal.get( Calendar.SECOND );
    }

    public void setSecond( int second )
    {
        cal.set( Calendar.SECOND, second );
    }

    public int getMilisecond()
    {
        return cal.get( Calendar.MILLISECOND );
    }

    public void setMilisecond( int milisecond )
    {
        cal.set( Calendar.MILLISECOND, milisecond );
    }


    public String toString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss:SSS" );
        return sdf.format( cal.getTime() );
    }

    public String toStringYYYYMMDDHHmmss()
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd: HH:mm:ss" );
        return sdf.format( cal.getTime() );
    }

    public String _getDDMMYYYY()
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
        return sdf.format( cal.getTime() );
    }


    public int _getTimeValue()
    {
        return getHour()*100 + getMinute();
    }

    public long _getTimeInMillis()
    {
        return cal.getTimeInMillis();
    }

    /*Added by kelum@codegen.net to saga branch
    * Copied here for use by ContractProductionService - dahamw@codegen.net*/
    public VegaDate _getDate()
    {
        return new VegaDate( getYear(),getMonth(),getDate());
    }


}
