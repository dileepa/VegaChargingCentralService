package lk.vega.charger.centralservice.client.web.dataLoader.user;

import lk.vega.charger.centralservice.client.web.domain.user.ChgUser;
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

    public static List<ClaimValue> getClaimValuesForChgUser( ChgUser chgUser )
    {
        List<ClaimValue> chgOwnerClaimsList = new ArrayList<ClaimValue>(  );

        ClaimValue titleClaim = new ClaimValue();
        titleClaim.setClaimURI( TITLE_URL );
        titleClaim.setValue( chgUser.getTitle() );

        ClaimValue genderClaim = new ClaimValue();
        genderClaim.setClaimURI( GENDER_URL );
        genderClaim.setValue( chgUser.getGender() );

        ClaimValue firstNameClaim = new ClaimValue();
        firstNameClaim.setClaimURI( FIRST_NAME_URL );
        firstNameClaim.setValue( chgUser.getFirstName() );

        ClaimValue lastNameClaim = new ClaimValue();
        lastNameClaim.setClaimURI( LAST_NAME_URL );
        lastNameClaim.setValue( chgUser.getLastName() );

        ClaimValue addressClaim = new ClaimValue();
        addressClaim.setClaimURI( ADDRESS_URL );
        addressClaim.setValue( chgUser.getAddress() );

        ClaimValue countryClaim = new ClaimValue();
        countryClaim.setClaimURI( COUNTRY_URL );
        countryClaim.setValue( chgUser.getCountry() );

        ClaimValue emailClaim = new ClaimValue();
        emailClaim.setClaimURI( EMAIL_URL );
        emailClaim.setValue( chgUser.getEmail() );

        ClaimValue telephoneClaim = new ClaimValue();
        telephoneClaim.setClaimURI( TELEPHONE_URL );
        telephoneClaim.setValue( chgUser.getTelephone() );

        ClaimValue mobileClaim = new ClaimValue();
        mobileClaim.setClaimURI( MOBILE_URL );
        mobileClaim.setValue( chgUser.getMobileNo() );

        ClaimValue organizationClaim = new ClaimValue();
        organizationClaim.setClaimURI( ORGANIZATION_NAME_URL );
        organizationClaim.setValue( chgUser.getOrganizationName() );

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
