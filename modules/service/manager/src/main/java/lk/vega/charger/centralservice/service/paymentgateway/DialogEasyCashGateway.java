package lk.vega.charger.centralservice.service.paymentgateway;

import lk.dialog.ezcash.payment.service.*;
import lk.vega.charger.util.ChgResponse;

import java.lang.Exception;


/**
 * Created by dileepa on 3/19/15.
 */
public class DialogEasyCashGateway implements PaymentGateWay
{
    public static String EZ_CASH_AGENT_TRANSACTIONS_SERVICE = "https://172.26.132.204/PaymentModule/ezcashagentservice?wsdl";
    public static String EZ_CASH_CUSTOMER_TRANSACTIONS_SERVICE = "https://172.26.132.204/PaymentModule/ezcashcustomerservice?wsdl";
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
    private static AuthenticationRequest authenticationRequest = null;
    private static String requestid=null;



    @Override
    public ChgResponse connect(PaymentDetail paymentDetail) throws Exception {
        EzcashAgentTransactionsImplServiceLocator serviceLocator = new EzcashAgentTransactionsImplServiceLocator();
        serviceLocator = new EzcashAgentTransactionsImplServiceLocator(EZ_CASH_AGENT_TRANSACTIONS_SERVICE, serviceLocator.getServiceName());
        EzcashAgentTransactions service = serviceLocator.getEzcashAgentTransactionsImplPort();
        ChgResponse chgResponse = new ChgResponse();
        if(service!=null) {
            paymentDetail.setService(service);
            chgResponse.setReturnData(service);
        }
        return null;
    }

    @Override public ChgResponse rollbackPayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public ChgResponse validatePayment( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse response = new ChgResponse();
        String authenticationKey = paymentDetail.getAuthenticationKey();
        if(authenticationKey.length()==10){
            return response;
        }
        return null;
    }

    @Override public ChgResponse validatePaymentWithHold( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public ChgResponse commitPayment( PaymentDetail paymentDetail ) throws Exception
    {
        try {
            EzcashAgentTransactions service = getAgentService();
            AgentTransactionRequest request = new AgentTransactionRequest();
            request.setAgentAlias("VEGA_AGENT");
            request.setAgentPin("1234");
            request.setAgentnotificationSend(true);
            request.setChannel(CHANNEL);
            request.setRequestId(requestid);
            request.setSubscriberMobile( paymentDetail.getAuthenticationKey() );
            request.setSubscribernotificationSend(false);
            request.setTxAmount(amount);
            request.setTxType("TX_AOTC");
            request.setAgentnotificationSend(false);

            SubmitTransactionRequest transactionRequest = new SubmitTransactionRequest(request);
            SubmitTransactionRequestResponse response = service.submitTransactionRequest(transactionRequest, getAuthenticationRequest());

/*            System.out.println("response ="+response.get_return().getEZCashRefId());
            System.out.println("response ="+response.get_return().getStatusDescription());
            System.out.println("response type="+response.get_return().getTxType());
            System.out.println("response owner="+response.get_return().getOwnerAlias());
            System.out.println("response status="+response.get_return().getStatus());*/
            if (response != null && response.get_return() != null && response.get_return().getStatus() == 62) {
                return response;
            }

        } catch (Exception e) {
           /* logger.error("Submit Transaction Error", e);*/
        }

        return null;
    }

    @Override public ChgResponse refundPayment( PaymentDetail paymentDetail ) throws Exception
    {
        return null;
    }

    @Override public String getPaymentGateWayType()
    {
        return PaymentGateWayFactory.DIALOG;
    }

}
