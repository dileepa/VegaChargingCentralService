package lk.vega.charger.userManagement;

import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.SOAPLoggingHandler;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServicePortType;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceUserStoreException_Exception;

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
    public static final String REMOTE_USER_STORE_MANAGER_SERVICE = "https://localhost:9443/services/RemoteUserStoreManagerService?wsdl";
    public static final String REMOTE_USER_STORE_MANAGER_NAMESPACE_URI = "http://service.ws.um.carbon.wso2.org";
    public static final String REMOTE_USER_STORE_MANAGER_SOAP11_PORT = "RemoteUserStoreManagerServiceHttpsSoap11Endpoint";
    public static final String REMOTE_USER_STORE_MANAGER_SOAP12_PORT = "RemoteUserStoreManagerServiceHttpsSoap12Endpoint";
    public static final String REMOTE_USER_STORE_MANAGER_HTTPS_PORT = "RemoteUserStoreManagerServiceHttpsEndpoint";
    public static final String USER_ADMIN_NAMESPACE_URI = "http://mgt.user.carbon.wso2.org";
    public static final String USER_ADMIN_USERNAME = "admin";
    public static final String USER_ADMIN_PASSWORD = "vega321";

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
        ServiceDelegate delegate = Provider.provider().createServiceDelegate( urlWsdl, serviceQName, RemoteUserStoreManagerServicePortType.class );

        QName portQName = new QName( REMOTE_USER_STORE_MANAGER_NAMESPACE_URI, REMOTE_USER_STORE_MANAGER_SOAP11_PORT );
        RemoteUserStoreManagerServicePortType remoteUserStoreManagerService = delegate.getPort( portQName, RemoteUserStoreManagerServicePortType.class );
        Map<String, Object> ctxt = ( (BindingProvider) remoteUserStoreManagerService ).getRequestContext();
        ctxt.put( BindingProvider.USERNAME_PROPERTY, USER_ADMIN_USERNAME );
        ctxt.put( BindingProvider.PASSWORD_PROPERTY, USER_ADMIN_PASSWORD );

        List<Handler> handlerChain = ( (BindingProvider) remoteUserStoreManagerService ).getBinding().getHandlerChain();
        handlerChain.add( new SOAPLoggingHandler() );
        ( (BindingProvider) remoteUserStoreManagerService ).getBinding().setHandlerChain( handlerChain );

        try {
            List<String> users = remoteUserStoreManagerService.listUsers("*", 100);
            for(String user: users){
                System.out.println("User : " + user);
            }
        } catch (RemoteUserStoreManagerServiceUserStoreException_Exception e) {
            e.printStackTrace();
        }

//        if( remoteUserStoreManagerService != null )
//        {
//            chgResponse.setNo( ChgResponse.SUCCESS );
//            chgResponse.setMsg( "Connection established Successfully" );
//            chgResponse.setReturnData( remoteUserStoreManagerService );
//        }
//        else
//        {
//            chgResponse.setNo( ChgResponse.ERROR );
//            chgResponse.setMsg( "Connection Failed" );
//        }
        return chgResponse;
    }


//    private String serverUrl = "https://localhost:9443/services/";
//
//    private AuthenticationAdminStub authstub = null;
//    private ConfigurationContext ctx;
//    private String authCookie = null;
//    private WSUserStoreManager remoteUserStoreManager = null;
//
//
//    /**
//     * Initialization of environment
//     *
//     * @throws Exception
//     */
//    public RemoteUMClient() throws Exception {
//        ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
//        String authEPR = serverUrl + "AuthenticationAdmin";
//        authstub = new AuthenticationAdminStub(ctx, authEPR);
//        ServiceClient client = authstub._getServiceClient();
//        Options options = client.getOptions();
//        options.setManageSession(true);
//        options.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, authCookie);
//    }
//
//    /**
//     * Authenticate to carbon as admin user and obtain the authentication cookie
//     *
//     * @param username
//     * @param password
//     * @return
//     * @throws Exception
//     */
//    public String login(String username, String password) throws Exception {
//        //String cookie = null;
//        boolean loggedIn = authstub.login(username, password, "localhost");
//        if (loggedIn) {
//            System.out.println("The user " + username + " logged in successfully.");
//            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(
//                    HTTPConstants.COOKIE_STRING);
//        } else {
//            System.out.println("Error logging in " + username);
//        }
//        return authCookie;
//    }
//
//    /**
//     * create web service client for RemoteUserStoreManager service from the wrapper api provided
//     * in carbon - remote-usermgt component
//     *
//     * @throws UserStoreException
//     */
//    public void createRemoteUserStoreManager() throws UserStoreException {
//
//        remoteUserStoreManager = new WSUserStoreManager(serverUrl, authCookie, ctx);
//    }
//
//    /**
//     * Add a user store to the system.
//     *
//     * @throws UserStoreException
//     */
//    public void addUser(String userName, String password) throws UserStoreException {
//
//        remoteUserStoreManager.addUser(userName, password, null, null, null);
//        System.out.println("Added user: " + userName);
//    }
//
//    /**
//     * Add a role to the system
//     *
//     * @throws Exception
//     */
//    public void addRole(String roleName) throws UserStoreException {
//        remoteUserStoreManager.addRole(roleName, null, null);
//        System.out.println("Added role: " + roleName);
//    }
//
//    /**
//     * Add a new user by assigning him to a new role
//     *
//     * @throws Exception
//     */
//    public void addUserWithRole(String userName, String password, String roleName)
//            throws UserStoreException {
//        remoteUserStoreManager.addUser(userName, password, new String[]{roleName}, null, null);
//        System.out.println("Added user: " + userName + " with role: " + roleName);
//    }
//
//    /**
//     * Retrieve all the users in the system
//     *
//     * @throws Exception
//     */
//    public String[] listUsers() throws UserStoreException {
//        return remoteUserStoreManager.listUsers("*", -1);
//    }
//
//    /**
//     * Delete an exisitng user from the system
//     *
//     * @throws Exception
//     */
//    public void deleteUser(String userName) throws UserStoreException {
//        remoteUserStoreManager.deleteUser(userName);
//        System.out.println("Deleted user:" + userName);
//    }
//
//    public static void main(String[] args) throws Exception {
//        //set trust store properties required in SSL communication.
//        String is_Home = ".." + File.separator + ".." + File.separator;
//        System.setProperty("javax.net.ssl.trustStore", is_Home + "repository" + File.separator + "resources" +
//                File.separator + "security" + File.separator + "wso2carbon.jks");
//        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
//
//        RemoteUMClient remoteUMClient = new RemoteUMClient();
//        //log in as admin user and obtain the cookie
//        remoteUMClient.login("admin", "admin");
//
//        /*Create client for RemoteUserStoreManagerService and perform user management operations*/
//
//        //create web service client
//        remoteUMClient.createRemoteUserStoreManager();
//        //add a new user to the system
//        remoteUMClient.addUser("kamal", "kamal");
//        //add a role to the system
//        remoteUMClient.addRole("eng");
//        //add a new user with a role
//        remoteUMClient.addUserWithRole("saman", "saman", "eng");
//        //print a list of all the users in the system
//        String[] users = remoteUMClient.listUsers();
//        System.out.println("List of users in the system:");
//        for (String user : users) {
//            System.out.println(user);
//        }
//        //delete an existing user
//        remoteUMClient.deleteUser("kamal");
//        //print the current list of users
//        String[] userList = remoteUMClient.listUsers();
//        System.out.println("List of users in the system currently:");
//        for (String user : userList) {
//            System.out.println(user);
//        }
//    }

}
