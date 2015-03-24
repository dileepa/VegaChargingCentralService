package lk.vega.charger.centralservice.service.paymentgateway;

/**
 * Created by dileepa on 3/20/15.
 */
public class PaymentDetail
{
    private String authenticationKey;
    private String transactionKey;

    public String getTransactionKey()
    {
        return transactionKey;
    }

    public void setTransactionKey( String transactionKey )
    {
        this.transactionKey = transactionKey;
    }

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
        this.transactionKey = null;
    }
}
