package lk.vega.charger.centralservice.client.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/13/15.
 * Time on 11:36 AM
 */
public class PasswordConstraintValidator implements ConstraintValidator<Password, String>
{
    @Override public void initialize( Password password )
    {

    }

    @Override public boolean isValid( String passwordFiled, ConstraintValidatorContext constraintValidatorContext )
    {
        if (passwordFiled == null)
        {
            return false;
        }

        return true;
    }
}
