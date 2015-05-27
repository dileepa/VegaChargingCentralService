package lk.vega.charger.centralservice.client.web.controller.level2Charger;

import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargePointTransactionStatusLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.level2Charger.LevelTwoChargerDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.ChgCustomerDataLoader;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationAvailabilityStatusBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;
import lk.vega.charger.centralservice.client.web.domain.miniChargeStation.MobileChargeLocationBean;
import lk.vega.charger.core.ChargeNetwork;
import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.core.ChargePointTransactionStatus;
import lk.vega.charger.core.ChgCustomerUser;
import lk.vega.charger.core.ChgLevelTwoTransaction;
import lk.vega.charger.core.ChgUser;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.ChgTimeStamp;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.DBUtility;
import lk.vega.charger.util.Savable;
import lk.vega.charger.util.connection.CHGConnectionPoolFactory;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/25/15.
 * Time on 11:20 AM
 */
@Controller
public class LevelTwoChargerController
{

    /**
     * @param chargePointId
     * @param rfId
     * @return IF SUCCESS, RETURN RESPONSE ACCORDING TO BELOW
     * <p/>
     * SUCCESS:TRS_ID:POINT_ID:REF_DID
     * <p/>
     * <p/>
     * IF ERROR, RETURN RESPONSE ACCORDING TO BELOW
     * <p/>
     * ERROR:ERROR_CODE:POINT_ID:REF_DID
     */

    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";


    public static final String ERROR_1 = "ERROR_1"; //INSUFFICIENT CREDIT TO CUSTOMER
    public static final String ERROR_2 = "ERROR_2"; //UNKNOWN ERROR
    public static final String ERROR_3 = "ERROR_3"; //CHARGE POINT IS NOT ACTIVE STATE
    public static final String ERROR_4 = "ERROR_4"; //CUSTOMER IS NOT ACTIVE STATE
    public static final String ERROR_5 = "ERROR_4"; //NETWORK IS NOT ALLOWED TO OTHERS FOR CHARGING


    @RequestMapping(value = "/LevelTwoChargerStartTransaction", method = RequestMethod.GET)
    public @ResponseBody String startTransaction( @RequestParam(value = "chgStationRef", required = false) String chgStationRef, @RequestParam(value = "rfId", required = false) String rfId )
    {

        String responseMsg = "";

        ChgResponse loadedChargePointResponse = ChargeStationLoader.loadSpecificChargePointByReference( chgStationRef );
        if( loadedChargePointResponse.isSuccess() )
        {
            ChargePoint chargePoint = (ChargePoint) loadedChargePointResponse.getReturnData();
            if( ChargeStationAvailabilityStatusBean.ACTIVE.equals( chargePoint.getChargePointAvailabilityStatus() ) )
            {

                ChgResponse loadCustomerRes = ChgCustomerDataLoader.loadCustomerByRFID( rfId );
                if( loadCustomerRes.isSuccess() )
                {
                    ChgCustomerUser chgCustomerUser = (ChgCustomerUser) loadCustomerRes.getReturnData();
                    if( ChgUser.ACTIVE_USER.equals( chgCustomerUser.getUserStatus() ) && chgCustomerUser.getCustomerPoints() > 0 )
                    {
                        ChgLevelTwoTransaction chgLevelTwoTransaction = new ChgLevelTwoTransaction();
                        chgLevelTwoTransaction.init();
                        chgLevelTwoTransaction.setTrsID( ChgLevelTwoTransaction.generateTransactionID( new ChgTimeStamp(), chgStationRef ) );
                        chgLevelTwoTransaction.setTrsStatus( ChgLevelTwoTransaction.TRS_STATUS_STARTED );
                        chgLevelTwoTransaction.setRfID( rfId );
                        chgLevelTwoTransaction.setCustomerUserName( chgCustomerUser.getUserName() );
                        chgLevelTwoTransaction.setChgPointId( chargePoint.getChargePointId() );
                        chgLevelTwoTransaction.setChgPointOwner( chargePoint.getUserName() );
                        chgLevelTwoTransaction.setChgPointRef( chargePoint.getReference() );

                        boolean isValid = isChargePointContainsInRFIDNetwork( chargePoint.getChargePointId(), rfId );

                        ChgResponse chgNetworkLoadRes = loadNetworkByChargePointId( chargePoint.getChargePointId() );
                        if( chgNetworkLoadRes.isSuccess() )
                        {
                            ChargeNetwork chargeNetwork = (ChargeNetwork) chgNetworkLoadRes.getReturnData();
                            if( isValid )
                            {
                                double transactionCostFromNetwork = chargeNetwork.getChargeAmount();
                                double transactionCostFromChargePoint = chargePoint.getChargeAmount();
                                double transactionCost = 0.0d;
                                boolean isChargeFromNetwork = false;
                                if( transactionCostFromChargePoint > transactionCostFromNetwork )
                                {
                                    transactionCost = transactionCostFromChargePoint;
                                    isChargeFromNetwork = false;
                                }
                                else
                                {
                                    transactionCost = transactionCostFromNetwork;
                                    isChargeFromNetwork = true;
                                }
                                responseMsg = transactionSave( transactionCost, isChargeFromNetwork, chgCustomerUser, chargeNetwork, chgLevelTwoTransaction, chargePoint, rfId );

                            }
                            else
                            {
                                boolean isAllowToOthers = chargeNetwork.getAllowToOthers();
                                if( isAllowToOthers )
                                {
                                    double transactionCostFromNetwork = chargeNetwork.getChargeAmountForOtherNetwork();
                                    double transactionCostFromChargePoint = chargePoint.getChargeAmount();
                                    double transactionCost = 0.0d;
                                    boolean isChargeFromNetwork = false;
                                    if( transactionCostFromChargePoint > transactionCostFromNetwork )
                                    {
                                        transactionCost = transactionCostFromChargePoint;
                                        isChargeFromNetwork = false;
                                    }
                                    else
                                    {
                                        transactionCost = transactionCostFromNetwork;
                                        isChargeFromNetwork = true;
                                    }
                                    responseMsg = transactionSave( transactionCost, isChargeFromNetwork, chgCustomerUser, chargeNetwork, chgLevelTwoTransaction, chargePoint, rfId );
                                }
                                else
                                {
                                    responseMsg = createErrorMessage( ERROR_5, chargePoint.getReference(), rfId );
                                }
                            }
                        }
                        else
                        {
                            //SOME CHG POINT HAS NO NETWORK , THEY WILL CHARGE BY OWN AMOUNT
                            double transactionCost = chargePoint.getChargeAmount();
                            responseMsg = transactionSave( transactionCost, false, chgCustomerUser, null, chgLevelTwoTransaction, chargePoint, rfId );
                        }
                    }
                    else
                    {
                        responseMsg = createErrorMessage( ERROR_4, chargePoint.getReference(), rfId );
                    }
                }
                else
                {
                    responseMsg = createErrorMessage( ERROR_2, chargePoint.getReference(), rfId );
                }
            }
            else
            {
                responseMsg = createErrorMessage( ERROR_3, chargePoint.getReference(), rfId );
            }
        }

        return responseMsg;
    }

    @RequestMapping(value = "/LevelTwoChargerStopTransaction", method = RequestMethod.GET)
    public @ResponseBody String stopTransaction( @RequestParam(value = "trsId", required = false) String trsId,@RequestParam(value = "chgStationRef", required = false) String chgStationRef, @RequestParam(value = "rfId", required = false) String rfId )
    {
        String resMsg = "";
        String trsStatus = ChgLevelTwoTransaction.TRS_STATUS_STARTED;
        ChgResponse chgResponse = LevelTwoChargerDataLoader.loadSpecificChargePointByPointID( trsId , trsStatus);
        if (chgResponse.isSuccess())
        {
            ChgLevelTwoTransaction chgLevelTwoTransaction = (ChgLevelTwoTransaction)chgResponse.getReturnData();
            chgLevelTwoTransaction.setStatus( Savable.MODIFIED );
            chgLevelTwoTransaction.setTrsStatus( ChgLevelTwoTransaction.TRS_STATUS_FINISHED );
            ChgResponse saveRes = CoreController.save( chgLevelTwoTransaction );
            if (saveRes.isSuccess())
            {
                ChgResponse response = ChargePointTransactionStatusLoader.loadSpecificChargePointTransactionStatusByReference(chgStationRef);
                if (response.isSuccess())
                {
                    ChargePointTransactionStatus chargePointTransactionStatus = (ChargePointTransactionStatus)response.getReturnData();
                    chargePointTransactionStatus.setChargePointWorkingStatus( ChargePointTransactionStatus.WORKING_STATUS_FREE );
                    chargePointTransactionStatus.setStatus( Savable.MODIFIED );
                    ChgResponse chargePointTransactionStatusRes = CoreController.save( chargePointTransactionStatus );
                    if (chargePointTransactionStatusRes.isSuccess())
                    {
                        resMsg = createSuccessMessage( chgLevelTwoTransaction.getTrsID(), chgStationRef, rfId );
                    }
                    else
                    {
                        resMsg = createErrorMessage( ERROR_2, chgStationRef, rfId );
                    }

                }
                else
                {

                    resMsg = createErrorMessage( ERROR_2, chgStationRef, rfId );
                }

            }
            else
            {
                resMsg = createErrorMessage( ERROR_2, chgStationRef, rfId );
            }

        }
        else
        {
            resMsg = createErrorMessage( ERROR_2, chgStationRef, rfId );
        }
        return resMsg;
    }


    public static boolean isChargePointContainsInRFIDNetwork( int chargeId, String ref )
    {
        boolean valid = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();
        sb.append( "SELECT TEMP.NFC_REF ,TEMP.CHG_NETWORK_ID,CHG_NETWORK_STATION_MAP.CHG_STATION_ID FROM " );
        sb.append( "(SELECT NFC_NETWORK_MAP.NFC_REF,NFC_NETWORK_MAP.CHG_NETWORK_ID  FROM CHG_USER_CUSTOMER " );
        sb.append( "INNER JOIN " );
        sb.append( "NFC_NETWORK_MAP " );
        sb.append( "ON " );
        sb.append( "CHG_USER_CUSTOMER.NFC_REF = NFC_NETWORK_MAP.NFC_REF " );
        sb.append( ") AS TEMP " );
        sb.append( "INNER JOIN " );
        sb.append( "CHG_NETWORK_STATION_MAP " );
        sb.append( "ON " );
        sb.append( "CHG_NETWORK_STATION_MAP.CHG_NETWORK_ID = TEMP.CHG_NETWORK_ID " );
        sb.append( "AND  " );
        sb.append( "NFC_REF = ? " );
        sb.append( "AND  " );
        sb.append( "CHG_STATION_ID = ?  " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setString( 1, ref );
            ps.setInt( 2, chargeId );
            rs = ps.executeQuery();
            if( rs.next() )
            {
                valid = true;
            }

        }
        catch( SQLException e )
        {
            e.printStackTrace();
            valid = false;
        }
        catch( Exception e )
        {
            e.printStackTrace();
            valid = false;
        }
        finally
        {
            DBUtility.close( rs );
            DBUtility.close( ps );
            DBUtility.close( con );
        }
        return valid;
    }


    //Assume One charge point has one Network only.
    public static ChgResponse loadNetworkByChargePointId( int chargePointId )
    {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb = new StringBuilder();
        sb.append( "SELECT * FROM CHG_NETWORK " );
        sb.append( "INNER JOIN " );
        sb.append( "CHG_NETWORK_STATION_MAP " );
        sb.append( "ON " );
        sb.append( "CHG_NETWORK_STATION_MAP.CHG_NETWORK_ID = CHG_NETWORK.NETWORK_ID " );
        sb.append( "AND " );
        sb.append( "CHG_NETWORK_STATION_MAP.CHG_STATION_ID = ? " );
        try
        {
            con = ( CHGConnectionPoolFactory.getCGConnectionPool( CHGConnectionPoolFactory.MYSQL ) ).getConnection();
            ps = con.prepareStatement( sb.toString() );
            ps.setInt( 1, chargePointId );
            rs = ps.executeQuery();
            if( rs.next() )
            {
                ChargeNetwork chargeNetwork = new ChargeNetwork();
                chargeNetwork.init();
                chargeNetwork.load( rs, con, 0 );
                return new ChgResponse( ChgResponse.SUCCESS, "Load Charging Stations Successfully", chargeNetwork );
            }

        }
        catch( SQLException e )
        {
            e.printStackTrace();
            return new ChgResponse( ChgResponse.ERROR, e.getMessage() );
        }
        catch( Exception e )
        {
            e.printStackTrace();
            return new ChgResponse( ChgResponse.ERROR, e.getMessage() );
        }
        finally
        {
            DBUtility.close( rs );
            DBUtility.close( ps );
            DBUtility.close( con );
        }

        return new ChgResponse( ChgResponse.ERROR, "UNKNOWN ERROR" );
    }

    private static String createErrorMessage( String errorCode, String ChargePointRef, String rfID )
    {
        StringBuilder error = new StringBuilder();
        error.append( ERROR );
        error.append( ":" );
        error.append( errorCode );
        error.append( ":" );
        error.append( ChargePointRef );
        error.append( ":" );
        error.append( rfID );
        return error.toString();
    }

    private static String createSuccessMessage( String trsID, String ChargePointRef, String rfID )
    {
        StringBuilder error = new StringBuilder();
        error.append( SUCCESS );
        error.append( ":" );
        error.append( trsID );
        error.append( ":" );
        error.append( ChargePointRef );
        error.append( ":" );
        error.append( rfID );
        return error.toString();
    }


    private String transactionSave( double transactionCost, boolean isChargeFromNetwork, ChgCustomerUser chgCustomerUser, ChargeNetwork chargeNetwork, ChgLevelTwoTransaction chgLevelTwoTransaction, ChargePoint chargePoint, String rfId )
    {
        String responseMsg = "";
        if( transactionCost <= chgCustomerUser.getCustomerPoints() )
        {
            if( isChargeFromNetwork )
            {
                chgLevelTwoTransaction.setNetworkID( chargeNetwork.getNetworkId() );
                chgLevelTwoTransaction.setNetworkOwner( chargeNetwork.getNetworkOwnerUserName() );
                chgLevelTwoTransaction.setNetworkRef( chargeNetwork.getReference() );
            }
            else
            {
                chgLevelTwoTransaction.setNetworkID( -1 );
                chgLevelTwoTransaction.setNetworkOwner( "DUMMY_NETWORK" );
                chgLevelTwoTransaction.setNetworkRef( "DUMMY_NETWORK" );
            }
            double remainPoints = chgCustomerUser.getCustomerPoints() - transactionCost;
            chgLevelTwoTransaction.setAmount( transactionCost );
            chgCustomerUser.setCustomerPoints( remainPoints );
            chgCustomerUser.setStatus( Savable.MODIFIED );
            chgLevelTwoTransaction.setStatus( Savable.NEW );
            ChgResponse customerSaveRes = CoreController.save( chgCustomerUser );
            if( customerSaveRes.isSuccess() )
            {
                ChgResponse trsSaveRes = CoreController.save( chgLevelTwoTransaction );
                if( trsSaveRes.isSuccess() )
                {
                    ChgResponse chgResponse = ChargePointTransactionStatusLoader.loadSpecificChargePointTransactionStatusByReference(chargePoint.getReference());
                    if (chgResponse.isSuccess())
                    {
                        ChargePointTransactionStatus chargePointTransactionStatus = (ChargePointTransactionStatus)chgResponse.getReturnData();
                        chargePointTransactionStatus.setChargePointWorkingStatus( ChargePointTransactionStatus.WORKING_STATUS_IN_PROGRESS );
                        chargePointTransactionStatus.setStatus( Savable.MODIFIED );
                        ChgResponse chargePointTransactionStatusRes = CoreController.save( chargePointTransactionStatus );
                        if (chargePointTransactionStatusRes.isSuccess())
                        {
                            responseMsg = createSuccessMessage( chgLevelTwoTransaction.getTrsID(), chargePoint.getReference(), rfId );
                        }
                        else
                        {
                            responseMsg = createErrorMessage( ERROR_2, chargePoint.getReference(), rfId );
                        }

                    }
                    else
                    {
                        responseMsg = createErrorMessage( ERROR_2, chargePoint.getReference(), rfId );
                    }

                }
                else
                {
                    responseMsg = createErrorMessage( ERROR_2, chargePoint.getReference(), rfId );
                }
            }
            else
            {
                responseMsg = createErrorMessage( ERROR_2, chargePoint.getReference(), rfId );
            }
        }
        else
        {
            responseMsg = createErrorMessage( ERROR_1, chargePoint.getReference(), rfId );
        }

        return responseMsg;
    }

    @RequestMapping(value = "/getActiveChargingPointLocations", method = RequestMethod.GET)
    public @ResponseBody List<MobileChargeLocationBean> getActiveChargingPointLocations()
    {
        ChgResponse chgResponse = ChargeStationLoader.loadAllChargePoints();
        List<MobileChargeLocationBean> mobileChargeLocationBeans = new ArrayList<MobileChargeLocationBean>(  );
        if (chgResponse.isSuccess())
        {
            List<ChargePoint> chargePointList = (List<ChargePoint>)chgResponse.getReturnData();
            for ( ChargePoint chargePoint : chargePointList)
            {
                if ( ChargeStationAvailabilityStatusBean.ACTIVE.equals( chargePoint.getChargePointAvailabilityStatus() ))
                {
                    ChargeStationBean chargeStationBean = new ChargeStationBean();
                    chargeStationBean.createBean( chargePoint );
                    MobileChargeLocationBean mobileChargeLocationBean = new MobileChargeLocationBean();
                    mobileChargeLocationBean.createBean( chargeStationBean );
                    mobileChargeLocationBeans.add( mobileChargeLocationBean );
                }
            }
        }
        return mobileChargeLocationBeans;
    }


    @RequestMapping(value = "/AllTransaction", method = RequestMethod.GET)
    public ModelAndView viewAllTransaction(SecurityContextHolderAwareRequestWrapper request)
    {
        ModelAndView modelAndView = new ModelAndView();
        ChgResponse transactionsRes = LevelTwoChargerDataLoader.loadAllTransactions(  );
        if( transactionsRes.isSuccess() )
        {
            List transactions = (List<ChgLevelTwoTransaction>) transactionsRes.getReturnData();
            modelAndView.getModel().put( "transactions", transactions );
            modelAndView.setViewName( "user/admin/allTransactions" );
        }

        return modelAndView;
    }

}
