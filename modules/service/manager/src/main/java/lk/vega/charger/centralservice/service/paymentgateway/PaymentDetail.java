package lk.vega.charger.centralservice.service.paymentgateway;

import lk.dialog.ezcash.payment.service.EzcashAgentTransactions;

/**
 * Created by dileepa on 3/20/15.
 */
public class PaymentDetail
{
    private String authenticationKey;
    private String transactionKey;
    private EzcashAgentTransactions service;
    private double finalAmount;
    private double initialAmount;


    public double getInitialAmount()
    {
        return initialAmount;
    }

    public void setInitialAmount( double initialAmount )
    {
        this.initialAmount = initialAmount;
    }

    public double getFinalAmount()
    {
        return finalAmount;
    }

    public void setFinalAmount( double finalAmount )
    {
        this.finalAmount = finalAmount;
    }

    public EzcashAgentTransactions getService() {
        return service;
    }

    public void setService(EzcashAgentTransactions service) {
        this.service = service;
    }

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
        finalAmount = 0.0d;
        initialAmount = 0.0d;
    }
}
