package lk.vega.charger.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * User: ruwan - copy from codegen
 * ChgDate: Dec 26, 2005
 * Time: 4:16:14 PM
 * Desc: If you set date by each field separately, follow the order year >> month >> date.
 *  Other wise you may get different date as output.
 */

public class ChgTimeStamp implements Comparable, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = -3553535558207438349L;
    private Calendar cal;

    public ChgTimeStamp()
    {
        this.cal = Calendar.getInstance();
    }

    public ChgTimeStamp(long millis)
    {
        this();
        this.cal.setTimeInMillis( millis );
    }


    public ChgTimeStamp(ChgDate ChgDate, ChgTime ChgTime)
    {
        this( ChgDate.getYear(), ChgDate.getMonth(), ChgDate.getDate(), ChgTime.getHour(), ChgTime.getMinute(), 0, 0 );
    }

    public ChgTimeStamp(java.sql.Timestamp timestamp)
    {
        this();
        cal.setTimeInMillis( timestamp.getTime() );
    }


    public ChgTimeStamp(java.util.Date date)
    {
        this();
        cal.setTimeInMillis( date.getTime() );
    }

    public ChgTimeStamp(java.sql.Date date)
    {
        this();
        cal.setTimeInMillis( date.getTime() );
    }

    public ChgTimeStamp(Calendar cal)
    {
        this.cal = cal;
    }

    /**
     * @param year  // offest from 1900 (as in java)
     * @param month // 0 - 11, as in java
     * @param date
     */
    public ChgTimeStamp(int year, int month, int date, int hour, int minute, int second, int millisecond)
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

    public boolean equals( ChgTimeStamp ChgTimeStamp)
    {
        if ( ( ChgTimeStamp._getCalendar().get( Calendar.YEAR ) == cal.get( Calendar.YEAR ) ) &&
                ( ChgTimeStamp._getCalendar().get( Calendar.MONTH ) == cal.get( Calendar.MONTH ) ) &&
                ( ChgTimeStamp._getCalendar().get( Calendar.DATE ) == cal.get( Calendar.DATE ) ) &&
                ( ChgTimeStamp._getCalendar().get( Calendar.HOUR_OF_DAY ) == cal.get( Calendar.HOUR_OF_DAY ) ) &&
                ( ChgTimeStamp._getCalendar().get( Calendar.MINUTE ) == cal.get( Calendar.MINUTE ) ) &&
                ( ChgTimeStamp._getCalendar().get( Calendar.SECOND ) == cal.get( Calendar.SECOND ) ) &&
                ( ChgTimeStamp._getCalendar().get( Calendar.MILLISECOND ) == cal.get( Calendar.MILLISECOND ) ) )
        {
            return true;
        }

        return false;
    }

    public boolean dateEquals( ChgDate ChgDate)
    {
        if ( ( ChgDate.getYear() == cal.get( Calendar.YEAR ) ) &&
                ( ChgDate.getMonth() == cal.get( Calendar.MONTH ) ) &&
                ( ChgDate.getDate() == cal.get( Calendar.DATE ) ) )
        {
            return true;
        }

        return false;
    }

    public boolean dateEquals( ChgTimeStamp VegaDate )
    {
        if ( ( VegaDate.getYear() == cal.get( Calendar.YEAR ) ) &&
                ( VegaDate.getMonth() == cal.get( Calendar.MONTH ) ) &&
                ( VegaDate.getDate() == cal.get( Calendar.DATE ) ) )
        {
            return true;
        }

        return false;
    }

    public boolean timeEquals( ChgTimeStamp ChgTimeStamp)
    {
        if ( ( ChgTimeStamp._getCalendar().get( Calendar.YEAR ) == cal.get( Calendar.YEAR ) ) &&
                ( ChgTimeStamp._getCalendar().get( Calendar.MONTH ) == cal.get( Calendar.MONTH ) ) &&
                ( ChgTimeStamp._getCalendar().get( Calendar.DATE ) == cal.get( Calendar.DATE ) ) &&
                ( ChgTimeStamp._getCalendar().get( Calendar.HOUR_OF_DAY ) == cal.get( Calendar.HOUR_OF_DAY ) ) &&
                ( ChgTimeStamp._getCalendar().get( Calendar.MINUTE ) == cal.get( Calendar.MINUTE ) ) )
        {
            return true;
        }

        return false;
    }

    public int compareTo( Object o )
    {
        //        if ( o instanceof ChgTimeStamp )
        //        {
        //            if ( after( ( ChgTimeStamp ) o ) )
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
        else if ( after( (ChgTimeStamp) o ) )
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    public boolean after( ChgDate date )
    {
        // if not the same date check by the calander after method
        //        return !equals( date ) && cal.after( date._getCalendar() );
        return !equals( date ) && cal.getTimeInMillis() > date._getTimeInMillis();
    }

    public boolean before( ChgDate date )
    {
        // if not the same date check by the calander before method
        return !equals( date ) && cal.getTimeInMillis() < date._getTimeInMillis();
    }

    public boolean after( ChgTimeStamp time )
    {
        // if not the same date check by the calander after method
        //        return !equals( date ) && cal.after( date._getCalendar() );
        return !equals( time ) && cal.getTimeInMillis() > time._getCalendar().getTimeInMillis();
    }

    public boolean before( ChgTimeStamp time )
    {
        // if not the same date check by the calander before method
        return !equals( time ) && cal.getTimeInMillis() < time._getCalendar().getTimeInMillis();
    }

    public Object clone()
    {
        return new ChgTimeStamp( getYear(), getMonth(), getDate(), getHour(), getMinute(), getSecond(), getMilisecond() );
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

    public int getDayOfYear()
    {
        return cal.get( Calendar.DAY_OF_YEAR );
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


    public String toStringYYYYMMDDHHmmss()
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss:SSS" );
        return sdf.format( cal.getTime() );
    }

    public String toString()
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
    public ChgDate _getDate()
    {
        return new ChgDate( getYear(),getMonth(),getDate());
    }

    public int getLastTwoDigitsOfYear ()
    {
        return (cal.get( Calendar.YEAR )%100);
    }

}
