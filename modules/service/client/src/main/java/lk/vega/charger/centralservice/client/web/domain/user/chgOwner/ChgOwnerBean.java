package lk.vega.charger.centralservice.client.web.domain.user.chgOwner;

import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.core.ChgUser;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/5/15.
 * Time on 12:06 PM
 */
public class ChgOwnerBean extends DomainBeanImpl
{
    private int userId;
    private String userName;
    private String email;
    private String telephone;
    private String mobileNo;
    private String password;

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

    @Override
    public void createBean( Object object )
    {
        ChgUser chgUser = (ChgUser)object;
        setUserId( chgUser.getUserId() );
        setUserName( chgUser.getUserName() );
        setEmail( chgUser.getEmail() );
        setTelephone( chgUser.getTelephone() );
        setMobileNo( chgUser.getMobileNo() );
    }

    @Override
    public void decodeBeanToReal( Object object )
    {
        ChgUser chgUser = (ChgUser)object;
        chgUser.init();
        chgUser.setUserId( getUserId() );
        chgUser.setUserName( getUserName() );
        chgUser.setEmail( getEmail() );
        chgUser.setTelephone( getTelephone() );
        chgUser.setMobileNo( getMobileNo() );
    }
}
