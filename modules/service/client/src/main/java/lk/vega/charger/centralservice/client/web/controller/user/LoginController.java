package lk.vega.charger.centralservice.client.web.controller.user;

import lk.vega.charger.centralservice.client.web.dataLoader.user.ChgUserDataLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.user.UserClaimAttributes;
import lk.vega.charger.centralservice.client.web.domain.user.ChgUserBean;
import lk.vega.charger.centralservice.client.web.permission.Security;
import lk.vega.charger.centralservice.client.web.permission.UserRoles;
import lk.vega.charger.util.ChgResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.wso2.carbon.um.ws.service.dao.xsd.ClaimDTO;

import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/11/15.
 * Time on 10:48 AM
 */
@Controller
public class LoginController
{
    @RequestMapping(value = "/Login", method = RequestMethod.GET)
    public ModelAndView loginView()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "user/login" );
        return modelAndView;
    }

    @RequestMapping(value = "/LoadUserProfile", method = RequestMethod.GET)
    public ModelAndView loadUserProfile( SecurityContextHolderAwareRequestWrapper request )
    {
        boolean isChargingOwner = UserRoles.isRoleExist( (List<GrantedAuthority>) ( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getAuthorities(), UserRoles.CHG_OWNER );
        boolean isChargingCustomer = UserRoles.isRoleExist( (List<GrantedAuthority>) ( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getAuthorities(), UserRoles.CHG_CUSTOMER );
        boolean isNetwork = UserRoles.isRoleExist( (List<GrantedAuthority>) ( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getAuthorities(), UserRoles.CHG_NETWORK );
        ModelAndView modelAndView = new ModelAndView();
        String userName = ( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getName();

        ChgResponse chgResponse = ChgUserDataLoader.loadUserProfileDetailsBySpecificUser( userName );
        modelAndView.setViewName( "user/chgCustomer/chgCustomerProfile" );
        if (chgResponse.isSuccess())
        {
            List<ClaimDTO> userClaims =  (List<ClaimDTO>)chgResponse.getReturnData();
            ChgUserBean chgUserBean = UserClaimAttributes.getChgUserBean( userClaims );
            modelAndView.getModel().put( "chgCustomerUser", chgUserBean );
        }
        return modelAndView;

    }

    @RequestMapping(value = "/LoginError", method = RequestMethod.GET)
    public ModelAndView loginErrorView( SecurityContextHolderAwareRequestWrapper request )
    {
        Exception loginErrorException = (Exception) request.getSession().getAttribute( Security.SPRING_SECURITY_LAST_EXCEPTION_KEY );
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "index/index" );
        modelAndView.getModel().put( "loginErrorMsg", loginErrorException.getMessage() );
        return modelAndView;
    }

    @RequestMapping(value = "/LoginSuccess", method = RequestMethod.GET)
    public ModelAndView loginSuccessView( SecurityContextHolderAwareRequestWrapper request )
    {
        if( request.getUserPrincipal() == null )
        {
            ModelAndView errorView = new ModelAndView(  );
            errorView.setViewName( "user/loginError" );
            errorView.getModel().put( "loginErrorMsg", Security.LOGIN_USER_ERROR );
            return errorView;
        }
        ModelAndView modelAndView = getLoginSuccesView( request );
        return modelAndView;
    }

    private ModelAndView getLoginSuccesView( SecurityContextHolderAwareRequestWrapper request )
    {
        boolean isAdmin = UserRoles.isRoleExist( (List<GrantedAuthority>) ( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getAuthorities(), UserRoles.CHG_ADMIN );
        boolean isChargingOwner = UserRoles.isRoleExist( (List<GrantedAuthority>) ( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getAuthorities(), UserRoles.CHG_OWNER );
        boolean isChargingCustomer = UserRoles.isRoleExist( (List<GrantedAuthority>) ( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getAuthorities(), UserRoles.CHG_CUSTOMER );
        boolean isNetwork = UserRoles.isRoleExist( (List<GrantedAuthority>) ( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getAuthorities(), UserRoles.CHG_NETWORK );
        ModelAndView modelAndView = new ModelAndView();
        if( isAdmin )
        {
            modelAndView.setViewName( "user/admin/adminLoginSuccess" );
        }
        else
        {
            if( isChargingOwner && isChargingCustomer && isNetwork )
            {
                //TODO view for both owner and customer
            }
            else if( isChargingCustomer && isNetwork )
            {
                //TODO view for both owner and customer
            }
            else if( isChargingOwner  && isNetwork )
            {
                //TODO view for both owner and customer
            }
            else if( isChargingOwner && isChargingCustomer )
            {
                //TODO view for both owner and customer
            }
            else if( isChargingCustomer )
            {
                modelAndView.setViewName( "user/chgCustomer/customerLoginSuccess" );
            }
            else if( isChargingOwner )
            {
                modelAndView.setViewName( "user/chgOwner/ownerLoginSuccess" );
            }
            else if( isNetwork )
            {
                modelAndView.setViewName( "user/chgNetwork/chgNetworkLoginSuccess" );
            }
            else
            {
                modelAndView.setViewName( "user/loginSuccess" );
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/Index", method = RequestMethod.GET)
    public ModelAndView initial( SecurityContextHolderAwareRequestWrapper request )
    {
        ModelAndView modelAndView = null;
        if( request.getUserPrincipal() == null )
        {
            modelAndView = new ModelAndView(  );
            modelAndView.setViewName( "index/index" );
            modelAndView.getModel().put( "loginErrorMsg", "" );
            return modelAndView;
//            return new ModelAndView("redirect:" + CoreController.CHG_CLINET_WEB_URL_VAL);
        }
        modelAndView = getLoginSuccesView( request );
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView mainPage( SecurityContextHolderAwareRequestWrapper request )
    {
       return initial( request );
    }

}
