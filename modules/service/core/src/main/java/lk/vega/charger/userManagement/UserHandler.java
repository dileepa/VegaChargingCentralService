package lk.vega.charger.userManagement;

import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.SOAPLoggingHandler;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServicePortType;

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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

/**
* Intelij Idea IDE
* Created by dileepa.
* Date on 5/5/15.
* Time on 12:24 PM
*/
public class UserHandler
{

    public static final String USER_ADMIN_SERVICE = "https://192.168.1.32:9443/services/UserAdmin?wsdl";
    public static final String REMOTE_USER_STORE_MANAGER_SERVICE = "https://192.168.1.32:9443/services/RemoteUserStoreManagerService?wsdl";
    public static final String REMOTE_USER_STORE_MANAGER_NAMESPACE_URI = "http://service.ws.um.carbon.wso2.org";
    public static final String REMOTE_USER_STORE_MANAGER_SOAP11_PORT = "RemoteUserStoreManagerServiceHttpsSoap11Endpoint";
    public static final String REMOTE_USER_STORE_MANAGER_SOAP12_PORT = "RemoteUserStoreManagerServiceHttpsSoap12Endpoint";
    public static final String REMOTE_USER_STORE_MANAGER_HTTPS_PORT = "RemoteUserStoreManagerServiceHttpsEndpoint";
    public static final String USER_ADMIN_NAMESPACE_URI = "http://mgt.user.carbon.wso2.org";
    public static final String USER_ADMIN_USERNAME = "admin";
    public static final String USER_ADMIN_PASSWORD = "admin";

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

    public static void main( String[] args )
    {
        ChgResponse chgResponse = new ChgResponse(  );
        try
        {
            chgResponse = connectToRemoteUserStoreManagerService();
        }
        catch( Exception e )
        {

            e.printStackTrace();
        }

        if (chgResponse.isSuccess())
        {
            System.out.println("success");
        }
    }

    public static ChgResponse connectToRemoteUserStoreManagerService() throws Exception
    {
        disableSslVerification();
//        System.setProperty("javax.net.ssl.trustStore", "/usr/local/vega/dev/app/wso2is-5.0.0/repository/resources/security/client-truststore.jks");
//        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        ChgResponse chgResponse = new ChgResponse(  );
        URL urlWsdl;
        try
        {
            urlWsdl = new URL( REMOTE_USER_STORE_MANAGER_SERVICE );
        }
        catch( MalformedURLException e )
        {
            throw new ConnectException( "Error in creating service URL", e );
        }


        QName serviceQName = new QName( REMOTE_USER_STORE_MANAGER_NAMESPACE_URI, "RemoteUserStoreManagerService" );
        Class provideClass = Class.forName( "com.sun.xml.internal.ws.spi.ProviderImpl" );
        Provider provider = (Provider) provideClass.newInstance();
        ServiceDelegate delegate = provider.createServiceDelegate( urlWsdl, serviceQName, RemoteUserStoreManagerServicePortType.class );

        QName portQName = new QName( REMOTE_USER_STORE_MANAGER_NAMESPACE_URI, REMOTE_USER_STORE_MANAGER_SOAP11_PORT );
        RemoteUserStoreManagerServicePortType remoteUserStoreManagerService = delegate.getPort( portQName, RemoteUserStoreManagerServicePortType.class );
        Map<String, Object> ctxt = ( (BindingProvider) remoteUserStoreManagerService ).getRequestContext();
        ctxt.put( BindingProvider.USERNAME_PROPERTY, USER_ADMIN_USERNAME );
        ctxt.put( BindingProvider.PASSWORD_PROPERTY, USER_ADMIN_PASSWORD );

        List<Handler> handlerChain = ( (BindingProvider) remoteUserStoreManagerService ).getBinding().getHandlerChain();
        handlerChain.add( new SOAPLoggingHandler() );
        ( (BindingProvider) remoteUserStoreManagerService ).getBinding().setHandlerChain( handlerChain );

//        try
//        {
//            List<String> users = remoteUserStoreManagerService.listUsers( "*", 100 );
//            for( String user : users )
//            {
//                System.out.println( "User : " + user );
//            }
//        }
//        catch( RemoteUserStoreManagerServiceUserStoreException_Exception e )
//        {
//            e.printStackTrace();
//        }

        if( remoteUserStoreManagerService != null )
        {
            chgResponse.setNo( ChgResponse.SUCCESS );
            chgResponse.setMsg( "Connection established Successfully" );
            chgResponse.setReturnData( remoteUserStoreManagerService );
        }
        else
        {
            chgResponse.setNo( ChgResponse.ERROR );
            chgResponse.setMsg( "Connection Failed" );
        }
        return chgResponse;
    }


}
