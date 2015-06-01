package lk.vega.charger.centralservice.client.web.validator;

import lk.vega.charger.core.NFCReference;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/22/15.
 * Time on 2:01 PM
 */
public class NFCReferenceConstraintValidator implements ConstraintValidator<lk.vega.charger.centralservice.client.web.validator.NFCReference, String>
{


    @Override public void initialize( lk.vega.charger.centralservice.client.web.validator.NFCReference nfcReference )
    {

    }

    @Override public boolean isValid( String nfcRef, ConstraintValidatorContext constraintValidatorContext )
    {
        if( nfcRef == null )
        {
            return false;
        }
        else if( nfcRef.length() == 0 )
        {
            return false;
        }
        else if( nfcRef.startsWith( NFCReference.DUMMY_NFC_REF ) )
        {
            return false;
        }

        return true;
    }
}
