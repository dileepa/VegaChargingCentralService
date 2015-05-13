package lk.vega.charger.centralservice.client.web.domain.user;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/5/15.
 * Time on 12:06 PM
 */
public class ChgUserBean
{
    private int userId;
    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;
    @NotEmpty
    private String verifyPassword;
    private String profileName;
    private String title;
    private String gender;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String address;
    @NotEmpty
    private String country;
    @NotEmpty @Email
    private String email;
    @NotEmpty
    private String telephone;
    @NotEmpty
    private String mobileNo;
    private String organizationName;
    private String userRole;

    public String getUserRole()
    {
        return userRole;
    }

    public void setUserRole( String userRole )
    {
        this.userRole = userRole;
    }

    public String getVerifyPassword()
    {
        return verifyPassword;
    }

    public void setVerifyPassword( String verifyPassword )
    {
        this.verifyPassword = verifyPassword;
    }

    public String getProfileName()
    {
        return profileName;
    }

    public void setProfileName( String profileName )
    {
        this.profileName = profileName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender( String gender )
    {
        this.gender = gender;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress( String address )
    {
        this.address = address;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry( String country )
    {
        this.country = country;
    }

    public String getOrganizationName()
    {
        return organizationName;
    }

    public void setOrganizationName( String organizationName )
    {
        this.organizationName = organizationName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String userName )
    {
        this.userName = userName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone( String telephone )
    {
        this.telephone = telephone;
    }

    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo( String mobileNo )
    {
        this.mobileNo = mobileNo;
    }

    public int getUserId()
    {

        return userId;
    }

    public void setUserId( int userId )
    {
        this.userId = userId;
    }


}
