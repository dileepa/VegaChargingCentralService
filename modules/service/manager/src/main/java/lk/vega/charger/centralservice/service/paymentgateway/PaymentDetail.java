package lk.vega.charger.centralservice.service.paymentgateway;

/**
 * Created by dileepa on 3/20/15.
 */
public class PaymentDetail
{
    private String authenticationKey;

    public String getAuthenticationKey()
    {
        return authenticationKey;
    }

    public void setAuthenticationKey( String authenticationKey )
    {
        this.authenticationKey = authenticationKey;
    }

    public void init ()
    {
        this.authenticationKey = null;
    }
}
