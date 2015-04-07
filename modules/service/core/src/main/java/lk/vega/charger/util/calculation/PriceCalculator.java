package lk.vega.charger.util.calculation;

/**
 * Created by dileepa on 4/7/15.
 */
public class PriceCalculator
{

    private String chargingCategory;
    private double nettRate = 23.45;

    public double getNettRate()
    {
        return nettRate;
    }

    public void setNettRate( double nettRate )
    {
        this.nettRate = nettRate;
    }

    public String getChargingCategory()
    {
        return chargingCategory;
    }

    public void setChargingCategory( String chargingCategory )
    {
        this.chargingCategory = chargingCategory;
    }

    public void printMessage(double finalAmount)
    {
        System.out.println( chargingCategory +"->"+finalAmount );
    }
}
