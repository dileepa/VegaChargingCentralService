package lk.vega.charger.security;

import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by dileepa on 4/6/15.
 */
public class AuthHandler implements SOAPHandler<SOAPMessageContext>
{

    public static final String USER_CREDENTIALS = "UserCredentials";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";


    @Override
    public Set<QName> getHeaders()
    {
        return null;
    }

    @Override
    public boolean handleMessage( SOAPMessageContext context )
    {
        boolean valid = false;
        Boolean isRequest = (Boolean) context.get( MessageContext.MESSAGE_OUTBOUND_PROPERTY );
        if( !isRequest )
        {
            try
            {
                String userName = "";
                String password = "";
                SOAPMessage soapMsg = context.getMessage();
                SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
                SOAPHeader soapHeader = soapEnv.getHeader();
                if( soapHeader == null )
                {
                    soapHeader = soapEnv.addHeader();
                    generateSOAPErrMessage( soapMsg, "SOAP Header is Not Found.." );
                }
                Iterator<SOAPHeaderElement> elements = soapHeader.examineAllHeaderElements();
                if( elements == null || !elements.hasNext() )
                {
                    generateSOAPErrMessage( soapMsg, "Empty Header Block found, No Header Attributes In Header Block." );
                    valid = false;
                }
                while( elements.hasNext() )
                {
                    SOAPHeaderElement element = elements.next();
                    if( element != null )
                    {
                        Name elementName = element.getElementName();
                        if( elementName != null )
                        {
                            String value = elementName.getLocalName();
                            if( USERNAME.equals( value ) )
                            {
                                userName = element.getValue();
                            }
                            else if( PASSWORD.equals( value ) )
                            {
                                password = element.getValue();
                            }
                        }
                    }
                }
                ChgResponse chgResponse = checkLogginSuccessfully( userName, password );
                if (chgResponse.isError())
                {
                    generateSOAPErrMessage( soapMsg, chgResponse.getMsg() );
                    valid = false;

                }
                else if (chgResponse.isSuccess())
                {
                    valid = true;
                }
                soapMsg.writeTo( System.out );  //TODO need to add log here...
            }
            catch( SOAPException e )
            {
                e.printStackTrace();
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }

        }
        return valid;
    }

    @Override
    public boolean handleFault( SOAPMessageContext context )
    {
        return false;
    }

    @Override
    public void close( MessageContext context )
    {

    }

    private void generateSOAPErrMessage( SOAPMessage msg, String reason )
    {
        try
        {
            SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
            soapBody.removeContents();
            SOAPFault soapFault = soapBody.addFault();
            soapFault.setFaultString( reason );
            throw new SOAPFaultException( soapFault );
        }
        catch( SOAPException e )
        {
            e.printStackTrace();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    /**
     * login success check
     * @param userName
     * @param password
     * @return
     */
    private ChgResponse checkLogginSuccessfully( String userName, String password )
    {
        ChgResponse chgResponse = new ChgResponse(  );
        chgResponse.setNo( ChgResponse.ERROR );
        chgResponse.setMsg( "Username and Password are incorrect." );
        if (CoreController.isNullOrEmpty( userName ) || CoreController.isNullOrEmpty( password ))
        {
            chgResponse.setMsg( "Invalid Username or Password found in Soap Header." );
        }
        if (CoreController.isNullOrEmpty( CoreController.CENTRAL_SERVICE_TRANSACTION_USERNAME_VAL ) || CoreController.isNullOrEmpty( CoreController.CENTRAL_SERVICE_TRANSACTION_PASSWORD_VAL ) )
        {
            chgResponse.setMsg( "Invalid Username or Password found in Service configuration" );
        }
        if( CoreController.CENTRAL_SERVICE_TRANSACTION_USERNAME_VAL.equals( userName ) && CoreController.CENTRAL_SERVICE_TRANSACTION_PASSWORD_VAL.equals( password ) )
        {
            chgResponse.setNo( ChgResponse.SUCCESS );
            chgResponse.setMsg( "Successfully Login to Central Service." );
        }
        return chgResponse;
    }
}
