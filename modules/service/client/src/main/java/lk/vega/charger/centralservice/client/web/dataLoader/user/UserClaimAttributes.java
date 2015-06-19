package lk.vega.charger.centralservice.client.web.dataLoader.user;

import lk.vega.charger.centralservice.client.web.domain.user.ChgUserBean;
import org.wso2.carbon.um.ws.service.dao.xsd.ClaimDTO;
import org.wso2.carbon.user.mgt.common.xsd.ClaimValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/8/15.
 * Time on 3:44 PM
 */
public class UserClaimAttributes
{
    public static final int CHG_OWNER_INDICATOR = 1;
    public static final int CHG_CUSTOMER_INDICATOR = 2;

    public static final String TITLE_URL = "http://wso2.org/claims/title";
    public static final String GENDER_URL = "http://wso2.org/claims/gender";
    public static final String FIRST_NAME_URL = "http://wso2.org/claims/givenname";
    public static final String LAST_NAME_URL = "http://wso2.org/claims/lastname";
    public static final String ADDRESS_URL = "http://wso2.org/claims/streetaddress";
    public static final String COUNTRY_URL = "http://wso2.org/claims/country";
    public static final String EMAIL_URL = "http://wso2.org/claims/emailaddress";
    public static final String TELEPHONE_URL = "http://wso2.org/claims/telephone";
    public static final String MOBILE_URL = "http://wso2.org/claims/mobile";
    public static final String ORGANIZATION_NAME_URL = "http://wso2.org/claims/organization";

    public static ChgUserBean getChgUserBean ( List<ClaimDTO> claimDTOs )
    {
        ChgUserBean chgUserBean = new ChgUserBean();
        for (ClaimDTO claimDTO : claimDTOs)
        {
            if (TITLE_URL.equals( claimDTO.getClaimUri() ))
            {
                chgUserBean.setTitle( claimDTO.getValue() );
            }
            else if (GENDER_URL.equals( claimDTO.getClaimUri() ))
            {
                 chgUserBean.setGender( claimDTO.getValue() );
            }
            else if (FIRST_NAME_URL.equals( claimDTO.getClaimUri() ))
            {
                chgUserBean.setFirstName( claimDTO.getValue() );
            }
            else if (LAST_NAME_URL.equals( claimDTO.getClaimUri() ))
            {
                chgUserBean.setLastName( claimDTO.getValue() );
            }
            else if (ADDRESS_URL.equals( claimDTO.getClaimUri() ))
            {
                chgUserBean.setAddress( claimDTO.getValue() );
            }
            else if (COUNTRY_URL.equals( claimDTO.getClaimUri() ))
            {
                chgUserBean.setCountry( claimDTO.getValue() );
            }
            else if (EMAIL_URL.equals( claimDTO.getClaimUri() ))
            {
                chgUserBean.setEmail( claimDTO.getValue() );
            }
            else if (TELEPHONE_URL.equals( claimDTO.getClaimUri() ))
            {
                chgUserBean.setTelephone( claimDTO.getValue() );
            }
            else if (MOBILE_URL.equals( claimDTO.getClaimUri() ))
            {
                chgUserBean.setMobileNo( claimDTO.getValue() );
            }
            else if (ORGANIZATION_NAME_URL.equals( claimDTO.getClaimUri() ))
            {
                chgUserBean.setOrganizationName( claimDTO.getValue() );
            }
        }
        return chgUserBean;
    }



    public static List<ClaimValue> getClaimValuesForChgUser( ChgUserBean chgUserBean )
    {
        List<ClaimValue> chgOwnerClaimsList = new ArrayList<ClaimValue>(  );

        ClaimValue titleClaim = new ClaimValue();
        titleClaim.setClaimURI( TITLE_URL );
        titleClaim.setValue( chgUserBean.getTitle() );

        ClaimValue genderClaim = new ClaimValue();
        genderClaim.setClaimURI( GENDER_URL );
        genderClaim.setValue( chgUserBean.getGender() );

        ClaimValue firstNameClaim = new ClaimValue();
        firstNameClaim.setClaimURI( FIRST_NAME_URL );
        firstNameClaim.setValue( chgUserBean.getFirstName() );

        ClaimValue lastNameClaim = new ClaimValue();
        lastNameClaim.setClaimURI( LAST_NAME_URL );
        lastNameClaim.setValue( chgUserBean.getLastName() );

        ClaimValue addressClaim = new ClaimValue();
        addressClaim.setClaimURI( ADDRESS_URL );
        addressClaim.setValue( chgUserBean.getAddress() );

        ClaimValue countryClaim = new ClaimValue();
        countryClaim.setClaimURI( COUNTRY_URL );
        countryClaim.setValue( chgUserBean.getCountry() );

        ClaimValue emailClaim = new ClaimValue();
        emailClaim.setClaimURI( EMAIL_URL );
        emailClaim.setValue( chgUserBean.getEmail() );

        ClaimValue telephoneClaim = new ClaimValue();
        telephoneClaim.setClaimURI( TELEPHONE_URL );
        telephoneClaim.setValue( chgUserBean.getTelephone() );

        ClaimValue mobileClaim = new ClaimValue();
        mobileClaim.setClaimURI( MOBILE_URL );
        mobileClaim.setValue( chgUserBean.getMobileNo() );

        ClaimValue organizationClaim = new ClaimValue();
        organizationClaim.setClaimURI( ORGANIZATION_NAME_URL );
        organizationClaim.setValue( chgUserBean.getOrganizationName() );

        chgOwnerClaimsList.add( titleClaim );
        chgOwnerClaimsList.add( genderClaim );
        chgOwnerClaimsList.add( firstNameClaim );
        chgOwnerClaimsList.add( lastNameClaim );
        chgOwnerClaimsList.add( addressClaim );
        chgOwnerClaimsList.add( countryClaim );
        chgOwnerClaimsList.add( emailClaim );
        chgOwnerClaimsList.add( telephoneClaim );
        chgOwnerClaimsList.add( mobileClaim );
        chgOwnerClaimsList.add( organizationClaim );

        return chgOwnerClaimsList;
    }

}
