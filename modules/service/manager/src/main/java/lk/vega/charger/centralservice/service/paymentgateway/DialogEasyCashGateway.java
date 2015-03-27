package lk.vega.charger.centralservice.service.paymentgateway;

import lk.dialog.ezcash.payment.service.AgentTransactionRequest;
import lk.dialog.ezcash.payment.service.AuthenticationRequest;
import lk.dialog.ezcash.payment.service.EzcashAgentTransactions;
import lk.dialog.ezcash.payment.service.GetTransactionStatusViaRequestId;
import lk.dialog.ezcash.payment.service.GetTransactionStatusViaRequestIdResponse;
import lk.dialog.ezcash.payment.service.RequestTransactionStatus;
import lk.dialog.ezcash.payment.service.SubmitTransactionRequest;
import lk.dialog.ezcash.payment.service.SubmitTransactionRequestResponse;
import lk.dialog.ezcash.payment.service.TransactionResponse;
import lk.vega.charger.util.ChgResponse;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.spi.Provider;
import javax.xml.ws.spi.ServiceDelegate;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;
import static lk.vega.charger.centralservice.service.transaction.TransactionController.phoneNumAmountAndDateSeparator;


/**
 * Created by dileepa on 3/19/15.
 */
public class DialogEasyCashGateway implements PaymentGateWay
{
    public static String EZ_CASH_AGENT_TRANSACTIONS_SERVICE = "https://172.26.132.204/PaymentModule/ezcashagentservice?wsdl";
    public static String EZ_CASH_USERNAME = "ivr_system";
    public static String EZ_CASH_PASSWORD = "password";
    public static int TRANSACTION_HISTORY_SIZE = 5;
    public static final int STATUS_CODE_SUCCESS = 5;
    private static final String PAYMENT_UTILITY = "TX_CUBP";
    private static final String PAYMENT_BILL = "TX_CTOB";
    private static final String PAYMENT_INSTITUTIONAL = "TX_CIMP";
    private static final String PAYMENT_MONEY = "TX_CTC";
    private static final String PAYMENT_MERCHANT = "TX_COTC";
    private static final String PAYMENT_VOUCHER = "TX_ACWT";
    private static final String CHANNEL = "WEB";

//    static
//    {
//        disableSslVerification();
//    }

    private static void disableSslVerification()
    {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }

                public void checkClientTrusted( X509Certificate[] certs, String authType )
                {
                }

                public void checkServerTrusted( X509Certificate[] certs, String authType )
                {
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance( "SSL" );
            sc.init( null, trustAllCerts, new java.security.SecureRandom() );
            HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier()
            {
                public boolean verify( String hostname, SSLSession session )
                {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier( allHostsValid );
        }
        catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch( KeyManagementException e )
        {
            e.printStackTrace();
        }
    }

    @Override
    public ChgResponse connect( PaymentDetail paymentDetail ) throws Exception
    {
        disableSslVerification();
        URL urlWsdl;
        try
        {
            urlWsdl = new URL( EZ_CASH_AGENT_TRANSACTIONS_SERVICE );
        }
        catch( MalformedURLException e )
        {
            throw new ConnectException( "Error in creating service URL", e );
        }
        QName serviceQName = new QName( "http://service.payment.ezcash.dialog.lk/", "EzcashAgentTransactionsImplService" );
        ServiceDelegate delegate = Provider.provider().createServiceDelegate( urlWsdl, serviceQName, EzcashAgentTransactions.class );

        QName portQName = new QName( "http://service.payment.ezcash.dialog.lk/", "EzcashAgentTransactionsImplPort" );
        EzcashAgentTransactions ezcashAgentTransactions = delegate.getPort( portQName, EzcashAgentTransactions.class );
        ChgResponse chgResponse = new ChgResponse();
        paymentDetail.setService( ezcashAgentTransactions );
        chgResponse.setReturnData( paymentDetail );
        List<Handler> handlerChain = ( (BindingProvider) ezcashAgentTransactions ).getBinding().getHandlerChain();
        handlerChain.add( new SOAPLoggingHandler() );
        ( (BindingProvider) ezcashAgentTransactions ).getBinding().setHandlerChain( handlerChain );

        if( ezcashAgentTransactions != null )
        {
            chgResponse.setNo( ChgResponse.SUCCESS );
            chgResponse.setMsg( "Connection established Successfully" );
        }
        else
        {
            chgResponse.setNo( ChgResponse.ERROR );
            chgResponse.setMsg( "Connection Failed" );
        }
        return chgResponse;
    }

    @Override public ChgResponse rollbackPayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public ChgResponse validatePayment( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse();
        String authenticationKey = paymentDetail.getAuthenticationKey();
        String phoneAmountTimeArray[] = phoneNumAmountAndDateSeparator( authenticationKey );
        String phoneNum = phoneAmountTimeArray[0];

        chgResponse.setReturnData( paymentDetail );
        if( phoneNum.length() == 10 )
        {
            chgResponse.setNo( ChgResponse.SUCCESS );
            chgResponse.setMsg( "validate Payment" );
        }
        else
        {
            chgResponse.setNo( ChgResponse.ERROR );
            chgResponse.setMsg( "Invalidate Payment" );
        }
        return chgResponse;
    }

    @Override public ChgResponse validatePaymentWithHold( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse();
        chgResponse.setReturnData( paymentDetail );
        chgResponse.setNo( ChgResponse.SUCCESS );
        chgResponse.setMsg( "validate Payment With Hold" );
        return chgResponse;
    }

    @Override public ChgResponse commitPayment( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse();
        chgResponse.setReturnData( paymentDetail );

        try
        {
            EzcashAgentTransactions service = paymentDetail.getService();
            AgentTransactionRequest request = new AgentTransactionRequest();

            String authenticationKey = paymentDetail.getAuthenticationKey();
            String phoneAmountTimeArray[] = phoneNumAmountAndDateSeparator( authenticationKey );
            String phoneNum = phoneAmountTimeArray[0];
            String initialAmount = phoneAmountTimeArray[1];

            request.setAgentAlias( "VEGA_AGENT" );
            request.setAgentPin( "1234" );
            request.setAgentnotificationSend( true );
            request.setChannel( CHANNEL );
            request.setRequestId( paymentDetail.getAuthenticationKey() );
            request.setSubscriberMobile( phoneNum.substring( 1 ) );
            request.setSubscribernotificationSend( false );
            request.setTxAmount( Double.valueOf( initialAmount ) );
            request.setTxType( "TX_AOTC" );
            request.setAgentnotificationSend( false );

            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            authenticationRequest.setUserName( EZ_CASH_USERNAME );
            authenticationRequest.setPassword( EZ_CASH_PASSWORD );

            SubmitTransactionRequest transactionRequest = new SubmitTransactionRequest();
            transactionRequest.setAgenttransactionrequest( request );
            SubmitTransactionRequestResponse response = service.submitTransactionRequest( transactionRequest, authenticationRequest );

            if( response != null && response.getReturn() != null && response.getReturn().getStatus() == 62 )
            {


                int transactionConfirmation = 0;
                TransactionResponse transactionResponse = null;

                try
                {
                    sleep( 10000 );
                }
                catch( InterruptedException ex )
                {
                    Logger.getLogger( DialogEasyCashGateway.class.getName() ).log( Level.SEVERE, null, ex );
                }
                try
                {
                    transactionResponse = getTransactionConfirmation( paymentDetail, authenticationRequest );
                    transactionConfirmation = transactionResponse.getStatus();
                }
                catch( RemoteException ex )
                {
                    Logger.getLogger( DialogEasyCashGateway.class.getName() ).log( Level.SEVERE, null, ex );
                }
                catch( Exception ex )
                {
                    Logger.getLogger( DialogEasyCashGateway.class.getName() ).log( Level.SEVERE, null, ex );
                }

                if( transactionConfirmation == 5 )
                {
                    chgResponse.setNo( ChgResponse.SUCCESS );
                    chgResponse.setReturnData( transactionResponse.getEZCashRefId() );
                    chgResponse.setMsg( "Payment Commited Successfully" );
                }
                else
                {
                    try
                    {
                        sleep( 10000 );
                    }
                    catch( InterruptedException ex )
                    {
                        Logger.getLogger( DialogEasyCashGateway.class.getName() ).log( Level.SEVERE, null, ex );
                    }
                    try
                    {
                        transactionResponse = getTransactionConfirmation( paymentDetail, authenticationRequest );
                        transactionConfirmation = transactionResponse.getStatus();
                    }
                    catch( RemoteException ex )
                    {
                        Logger.getLogger( DialogEasyCashGateway.class.getName() ).log( Level.SEVERE, null, ex );
                    }
                    catch( Exception ex )
                    {
                        Logger.getLogger( DialogEasyCashGateway.class.getName() ).log( Level.SEVERE, null, ex );
                    }

                    if( transactionConfirmation == 5 )
                    {
                        chgResponse.setNo( ChgResponse.SUCCESS );
                        chgResponse.setReturnData( transactionResponse.getEZCashRefId() );
                        chgResponse.setMsg( "Payment Commited Successfully" );
                    }
                    else
                    {
                        try
                        {
                            sleep( 10000 );
                        }
                        catch( InterruptedException ex )
                        {
                            Logger.getLogger( DialogEasyCashGateway.class.getName() ).log( Level.SEVERE, null, ex );
                        }
                        try
                        {
                            transactionResponse = getTransactionConfirmation( paymentDetail, authenticationRequest );
                            transactionConfirmation = transactionResponse.getStatus();
                        }
                        catch( RemoteException ex )
                        {
                            Logger.getLogger( DialogEasyCashGateway.class.getName() ).log( Level.SEVERE, null, ex );
                        }
                        catch( Exception ex )
                        {
                            Logger.getLogger( DialogEasyCashGateway.class.getName() ).log( Level.SEVERE, null, ex );
                        }


                        if( transactionConfirmation == 5 )
                        {
                            chgResponse.setNo( ChgResponse.SUCCESS );
                            chgResponse.setReturnData( transactionResponse.getEZCashRefId() );
                            chgResponse.setMsg( "Payment Commited Successfully" );
                        }
                        else
                        {
                            chgResponse.setNo( ChgResponse.ERROR );
                            chgResponse.setMsg( "Invalidate Payment" );
                        }
                    }
                }


            }
            else
            {
                chgResponse.setNo( ChgResponse.ERROR );
                chgResponse.setMsg( "Invalidate Payment" );
            }

        }
        catch( Exception e )
        {
            chgResponse.setNo( ChgResponse.ERROR );
            chgResponse.setMsg( "Exception Fired" );
        }
        return chgResponse;
    }

    @Override public ChgResponse refundPayment( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse();
        chgResponse.setReturnData( paymentDetail );

        try
        {
            EzcashAgentTransactions service = paymentDetail.getService();
            AgentTransactionRequest request = new AgentTransactionRequest();

            String authenticationKey = paymentDetail.getAuthenticationKey();
            String phoneAmountTimeArray[] = phoneNumAmountAndDateSeparator( authenticationKey );
            String phoneNum = phoneAmountTimeArray[0].substring( 1 );
            String initialAmount = phoneAmountTimeArray[1];

            request.setAgentAlias( "VEGA_AGENT" );
            request.setAgentPin( "1234" );
            request.setAgentnotificationSend( true );
            request.setChannel( CHANNEL );
            request.setRequestId( paymentDetail.getAuthenticationKey() );
            request.setSubscriberMobile( phoneNum );
            request.setSubscribernotificationSend( true );
            request.setTxAmount( 50.0 );   //TODO load final value from chargingTransaction object.
            request.setTxType( "TX_ACWT" );
            request.setAgentnotificationSend( false );

            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            authenticationRequest.setUserName( EZ_CASH_USERNAME );
            authenticationRequest.setPassword( EZ_CASH_PASSWORD );

            SubmitTransactionRequest transactionRequest = new SubmitTransactionRequest();
            transactionRequest.setAgenttransactionrequest( request );
            SubmitTransactionRequestResponse response = service.submitTransactionRequest( transactionRequest, authenticationRequest );

            if( response != null && response.getReturn() != null && response.getReturn().getStatus() == 5 )
            {
                chgResponse.setNo( ChgResponse.SUCCESS );
                chgResponse.setMsg( "Payment Commited Successfully" );
                chgResponse.setReturnData( response.getReturn().getEZCashRefId() );
            }
            else
            {
                chgResponse.setNo( ChgResponse.ERROR );
                chgResponse.setMsg( "Invalidate Payment" );
            }

        }
        catch( Exception e )
        {
            chgResponse.setNo( ChgResponse.ERROR );
            chgResponse.setMsg( "Exception Fired" );
        }
        return chgResponse;
    }

    @Override public String getPaymentGateWayType()
    {
        return PaymentGateWayFactory.DIALOG;
    }

    public static TransactionResponse getTransactionConfirmation( PaymentDetail paymentDetail, AuthenticationRequest authenticationRequest ) throws RemoteException
    {
        EzcashAgentTransactions service = paymentDetail.getService();
        RequestTransactionStatus requestTransactionStatus = new RequestTransactionStatus();
        requestTransactionStatus.setOwnerAlias( "VEGA_AGENT" );
        requestTransactionStatus.setOwnerPin( "1234" );
        requestTransactionStatus.setRequestId( paymentDetail.getAuthenticationKey() );

        GetTransactionStatusViaRequestId transactionStatusViaRequestId = new GetTransactionStatusViaRequestId();
        transactionStatusViaRequestId.setRequesttransactionstatus( requestTransactionStatus );
        GetTransactionStatusViaRequestIdResponse transactionStatusViaRequestIdResponse = service.getTransactionStatusViaRequestId( transactionStatusViaRequestId, authenticationRequest );
        return transactionStatusViaRequestIdResponse.getReturn();
    }

    public static void main( String[] args )
    {
        DialogEasyCashGateway dialogEasyCashGateway = new DialogEasyCashGateway();
        try
        {
            ChgResponse chgResponse = dialogEasyCashGateway.connect( new PaymentDetail() );
            System.out.println( chgResponse );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

}
