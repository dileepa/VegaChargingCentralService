package lk.vega.charger.centralservice.service.paymentgateway;

import lk.dialog.ezcash.payment.service.*;
import lk.vega.charger.util.ChgResponse;

import java.lang.Exception;
import java.rmi.RemoteException;
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



    @Override
    public ChgResponse connect(PaymentDetail paymentDetail) throws Exception {
        EzcashAgentTransactionsImplService serviceLocator = new EzcashAgentTransactionsImplService();
        EzcashAgentTransactions service = serviceLocator.getEzcashAgentTransactionsImplPort();
        ChgResponse chgResponse = new ChgResponse();
        paymentDetail.setService(service);
        chgResponse.setReturnData(paymentDetail);

        if(service!=null) {
            chgResponse.setNo(ChgResponse.SUCCESS);
            chgResponse.setMsg("Connection established Successfully");
        }
        else{
            chgResponse.setNo(ChgResponse.ERROR);
            chgResponse.setMsg("Connection Failed");
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

        chgResponse.setReturnData(paymentDetail);
        if(phoneNum.length()==10){
            chgResponse.setNo(ChgResponse.SUCCESS);
            chgResponse.setMsg("validate Payment");
        }
        else{
            chgResponse.setNo(ChgResponse.ERROR);
            chgResponse.setMsg("Invalidate Payment");
        }
        return chgResponse;
    }

    @Override public ChgResponse validatePaymentWithHold( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse();
        chgResponse.setReturnData(paymentDetail);
        chgResponse.setNo(ChgResponse.SUCCESS);
        chgResponse.setMsg("validate Payment With Hold");
        return chgResponse;
    }

    @Override public ChgResponse commitPayment( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse();
        chgResponse.setReturnData(paymentDetail);

        try {
            EzcashAgentTransactions service = paymentDetail.getService();
            AgentTransactionRequest request = new AgentTransactionRequest();

            String authenticationKey = paymentDetail.getAuthenticationKey();
            String phoneAmountTimeArray[] = phoneNumAmountAndDateSeparator( authenticationKey );
            String phoneNum = phoneAmountTimeArray[0];
            String initialAmount = phoneAmountTimeArray[1];

            request.setAgentAlias("VEGA_AGENT");
            request.setAgentPin("1234");
            request.setAgentnotificationSend(true);
            request.setChannel(CHANNEL);
            request.setRequestId(paymentDetail.getAuthenticationKey());
            request.setSubscriberMobile( phoneNum );
            request.setSubscribernotificationSend(false);
            request.setTxAmount(Double.valueOf(initialAmount));
            request.setTxType("TX_AOTC");
            request.setAgentnotificationSend(false);

            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            authenticationRequest.setUserName(EZ_CASH_USERNAME);
            authenticationRequest.setPassword(EZ_CASH_PASSWORD);

            SubmitTransactionRequest transactionRequest = new SubmitTransactionRequest();
            transactionRequest.setAgenttransactionrequest(request);
            SubmitTransactionRequestResponse response = service.submitTransactionRequest(transactionRequest, authenticationRequest);

            if (response != null && response.getReturn() != null && response.getReturn().getStatus() == 62) {


                int transactionConfirmation = 0;

                try {
                    sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DialogEasyCashGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    transactionConfirmation = getTransactionConfirmation( paymentDetail,authenticationRequest);
                } catch (RemoteException ex) {
                    Logger.getLogger(DialogEasyCashGateway.class.getName()).log(Level.SEVERE, null, ex);
                }catch (Exception ex) {
                    Logger.getLogger(DialogEasyCashGateway.class.getName()).log(Level.SEVERE, null, ex);
                }

                if(transactionConfirmation==5){
                    chgResponse.setNo(ChgResponse.SUCCESS);
                    chgResponse.setMsg("Payment Commited Successfully");
                }
                else {
                    try {
                        sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DialogEasyCashGateway.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        transactionConfirmation = getTransactionConfirmation( paymentDetail,authenticationRequest);
                    } catch (RemoteException  ex) {
                        Logger.getLogger(DialogEasyCashGateway.class.getName()).log(Level.SEVERE, null, ex);
                    }catch (Exception ex) {
                        Logger.getLogger(DialogEasyCashGateway.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (transactionConfirmation == 5) {
                        chgResponse.setNo(ChgResponse.SUCCESS);
                        chgResponse.setMsg("Payment Commited Successfully");
                    } else {
                        try {
                            sleep(10000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(DialogEasyCashGateway.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            transactionConfirmation = getTransactionConfirmation( paymentDetail,authenticationRequest);
                        } catch (RemoteException ex) {
                            Logger.getLogger(DialogEasyCashGateway.class.getName()).log(Level.SEVERE, null, ex);
                        }catch (Exception ex) {
                            Logger.getLogger(DialogEasyCashGateway.class.getName()).log(Level.SEVERE, null, ex);
                        }


                        if (transactionConfirmation == 5) {
                            chgResponse.setNo(ChgResponse.SUCCESS);
                            chgResponse.setMsg("Payment Commited Successfully");
                        } else {
                            chgResponse.setNo(ChgResponse.ERROR);
                            chgResponse.setMsg("Invalidate Payment");
                        }
                    }
                }


            }
            else{
                chgResponse.setNo(ChgResponse.ERROR);
                chgResponse.setMsg("Invalidate Payment");
            }

        } catch (Exception e) {
            chgResponse.setNo(ChgResponse.ERROR);
            chgResponse.setMsg("Exception Fired");
        }
        return chgResponse;
    }

    @Override public ChgResponse refundPayment( PaymentDetail paymentDetail ) throws Exception
    {
        ChgResponse chgResponse = new ChgResponse();
        chgResponse.setReturnData(paymentDetail);

        try {
            EzcashAgentTransactions service = paymentDetail.getService();
            AgentTransactionRequest request = new AgentTransactionRequest();

            String authenticationKey = paymentDetail.getAuthenticationKey();
            String phoneAmountTimeArray[] = phoneNumAmountAndDateSeparator( authenticationKey );
            String phoneNum = phoneAmountTimeArray[0];
            String initialAmount = phoneAmountTimeArray[1];

            request.setAgentAlias("VEGA_AGENT");
            request.setAgentPin("1234");
            request.setAgentnotificationSend(true);
            request.setChannel(CHANNEL);
            request.setRequestId(paymentDetail.getAuthenticationKey());
            request.setSubscriberMobile( phoneNum );
            request.setSubscribernotificationSend(true);
            request.setTxAmount(Double.valueOf(initialAmount));
            request.setTxType("TX_ACWT");
            request.setAgentnotificationSend(false);

            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            authenticationRequest.setUserName(EZ_CASH_USERNAME);
            authenticationRequest.setPassword(EZ_CASH_PASSWORD);

            SubmitTransactionRequest transactionRequest = new SubmitTransactionRequest();
            transactionRequest.setAgenttransactionrequest(request);
            SubmitTransactionRequestResponse response = service.submitTransactionRequest(transactionRequest, authenticationRequest);

            if (response != null && response.getReturn() != null && response.getReturn().getStatus() == 62) {
                chgResponse.setNo(ChgResponse.SUCCESS);
                chgResponse.setMsg("Payment Commited Successfully");
            }
            else{
                chgResponse.setNo(ChgResponse.ERROR);
                chgResponse.setMsg("Invalidate Payment");
            }

        } catch (Exception e) {
            chgResponse.setNo(ChgResponse.ERROR);
            chgResponse.setMsg("Exception Fired");
        }
        return chgResponse;
    }

    @Override public String getPaymentGateWayType()
    {
        return PaymentGateWayFactory.DIALOG;
    }

    public static int getTransactionConfirmation(PaymentDetail paymentDetail ,  AuthenticationRequest authenticationRequest) throws RemoteException{
        EzcashAgentTransactions service =paymentDetail.getService();
        RequestTransactionStatus requestTransactionStatus = new RequestTransactionStatus();
        requestTransactionStatus.setOwnerAlias("VEGA_AGENT");
        requestTransactionStatus.setOwnerPin("1234");
        requestTransactionStatus.setRequestId(paymentDetail.getAuthenticationKey());

        GetTransactionStatusViaRequestId transactionStatusViaRequestId = new GetTransactionStatusViaRequestId();
        transactionStatusViaRequestId.setRequesttransactionstatus(requestTransactionStatus);
        GetTransactionStatusViaRequestIdResponse transactionStatusViaRequestIdResponse = service.getTransactionStatusViaRequestId(transactionStatusViaRequestId, authenticationRequest);
        return transactionStatusViaRequestIdResponse.getReturn().getStatus();
    }

}
