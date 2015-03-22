package lk.vega.charger.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * User: ruwan - copy from codegen
 * ChgDate: Dec 26, 2005
 * Time: 4:16:14 PM
 * Desc: If you set date by each field separately, follow the order year >> month >> date.
 *  Other wise you may get different date as output.
 */
public class ChgDate implements Comparable<ChgDate>, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = -8227919564044277655L;
    private WeakReference<Calendar> calRef;
    private int date;
    private int month;
    private int year;
    private int _intValue;

    public void add(int field, int amount)
    {
        Calendar cal = initCal();
        cal.add( field, amount);

        date = cal.get( Calendar.DATE );
        month = cal.get( Calendar.MONTH );
        year = cal.get( Calendar.YEAR );
        calclulateIntValue();
    }

    private void calclulateIntValue()
    {
        _intValue = year * 10000 + (month +1) * 100 + date;
    }

    public int _getIntValue()
    {
        return _intValue;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null ) return false;

        if( !( o instanceof ChgDate) )
        {
            return false;
        }

        return this._intValue == ((ChgDate) o)._intValue;
    }


    public int hashCode()
    {
        int result;
        result = getYear();
        result = 29 * result + getMonth();
        result = 29 * result + getDate();
        return result;
    }

    public ChgDate()
    {
        Calendar cal = Calendar.getInstance();
        date = cal.get( Calendar.DATE );
        month = cal.get( Calendar.MONTH );
        year = cal.get( Calendar.YEAR );

        calclulateIntValue();
    }

    public ChgDate(Calendar cal)
    {
        _setTimeInMillis( cal.getTimeInMillis() );
    }

    public ChgDate(long time)
    {
        _setTimeInMillis( time );

    }

    public ChgDate(java.util.Date d)
    {
        _setTimeInMillis( d.getTime() );
    }

    public ChgDate(java.sql.Date d)
    {
        _setTimeInMillis( d.getTime() );
    }

    /**
     * @param year
     * @param month // 0 - 11, as in java
     * @param date
     */
    public ChgDate(int year, int month, int date)
    {
        this.year = year;
        this.month = month;
        this.date = date;
        calclulateIntValue();
    }

    public Object clone()
    {
        return new ChgDate( getYear(), getMonth(), getDate() );
    }

    public long _getTimeInMillis()
    {
        Calendar cal = initCal();
        return cal.getTimeInMillis();
    }

    public void  _setTimeInMillis( long millis )
    {
        Calendar cal = initCal();
        cal.setTimeInMillis( millis );
        cal.set( Calendar.HOUR_OF_DAY, 12 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.MILLISECOND, 0 );

        date = cal.get( Calendar.DATE );
        month = cal.get( Calendar.MONTH );
        year = cal.get( Calendar.YEAR );

        calclulateIntValue();
    }

    public boolean after( ChgDate date)
    {
        // if not the same date check by the calander after method
        // return !equals( date ) && cal.getTimeInMillis() > date._getTimeInMillis();
        return _intValue > date._getIntValue();
    }

    public boolean before( ChgDate date)
    {
        // if not the same date check by the calander before method
        //return !equals( date ) && cal.getTimeInMillis() < date._getTimeInMillis();
        return _intValue < date._getIntValue();
    }

    public boolean onOrBefore( ChgDate date )
    {
        //return date.equals( this ) || before( date );
        return _intValue <= date._getIntValue();
    }

    public boolean onOrAfter( ChgDate date )
    {
        //return date.equals( this ) || after( date );
        return _intValue >= date._getIntValue();
    }

    public int _get( int field )
    {
        Calendar cal = initCal();
        return cal.get( field );
    }

    public void _set( int field, int value )
    {
        Calendar cal = initCal();
        cal.set( field, value );
        date = cal.get( Calendar.DATE );
        month = cal.get( Calendar.MONTH );
        year = cal.get( Calendar.YEAR );

        calclulateIntValue();
    }

    public java.sql.Date _getSQLDate()
    {
        Calendar cal = initCal();
        return new java.sql.Date( cal.getTimeInMillis() );
    }

    public void _setSQLDate(java.sql.Date d)
    {
        _setTimeInMillis( d.getTime() );
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
        this.calRef = null;
        calclulateIntValue();
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
        this.calRef = null;
        calclulateIntValue();
    }

    public int getDate()
    {
        return date;
    }

    public void setDate(int date)
    {
        this.date = date;
        this.calRef = null;
        calclulateIntValue();
    }


    public String toString()
    {
        return toKey();
    }

    /*
     * To be used when date string is used as a key for comparisons across client/ server
     * Note: the output of toString() can be changed in client side according to the locale
      * Therefore toString() is no longer valid for key comparisons
     */
    public String toKey()
    {
        StringBuilder sb = new StringBuilder();
        if( getDate() < 10 )
        {
            sb.append( "0" );
        }
        sb.append( getDate() ).append( "/" );
        if( getMonth() + 1 < 10 )
        {
            sb.append( "0" );
        }
        sb.append( getMonth() + 1 ).append( "/" );
        sb.append( getYear() );

        return sb.toString();
    }


    /* Added by Dasun for date mm/dd//yy format */
    public String toStringMMDDYY()
    {
        StringBuilder sb = new StringBuilder();
        if ( getMonth() + 1  < 10 ) sb.append( "0" );
        sb.append ( getMonth() + 1  ).append( "/" );
        if ( getDate() < 10 ) sb.append( "0" );
        sb.append ( getDate() ).append( "/" );
        sb.append ( getYear() );

        return sb.toString();
    }

    /* Added by kelum@codegen.net  for datedd-MMM-yyyy format  to saga branch
    * Copied here for use by ContractProductionService - dahamw@codegen.net
    * */
    public String toStringDDMMMYYYY()
    {
        SimpleDateFormat datef1 = new SimpleDateFormat( "dd-MMM-yyyy" );
        return datef1.format( this._getSQLDate() );
    }

    public int compareTo( ChgDate o)
    {
        if ( equals( o ))
        {
            return 0;
        }
        else if ( after( o ) )
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    @Deprecated
    public final Calendar _getCalendar()
    {
        return initCal();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        this.calRef = null;
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }

    private Calendar initCal ()
    {
        Calendar tempCal = calRef == null ? null : calRef.get();

        if ( tempCal == null )
        {
            tempCal = Calendar.getInstance(  );

            tempCal.set( Calendar.YEAR, year );
            tempCal.set( Calendar.MONTH, month );
            tempCal.set( Calendar.DATE, date );
            tempCal.set( Calendar.HOUR_OF_DAY, 12 );
            tempCal.set( Calendar.MINUTE, 0 );
            tempCal.set( Calendar.SECOND, 0 );
            tempCal.set( Calendar.MILLISECOND, 0 );

            this.calRef = new WeakReference<Calendar>( tempCal );
        }

        return tempCal;
    }

    public String _getDateString()
    {
        SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateInstance( DateFormat.MEDIUM);
        return format.format( this._getSQLDate() );
    }

    public XMLGregorianCalendar _getXmlGregorianCalendar()
    {
        XMLGregorianCalendar xmlCal = null;
        try
        {
            xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            xmlCal.setDay( getDate() );
            xmlCal.setMonth( getMonth() +1 );
            xmlCal.setYear( getYear() );
        }
        catch( DatatypeConfigurationException e )
        {
            e.printStackTrace();
        }

        return xmlCal;
    }
}
