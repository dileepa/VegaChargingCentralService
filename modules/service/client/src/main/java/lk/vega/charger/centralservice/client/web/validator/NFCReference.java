package lk.vega.charger.centralservice.client.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 5/22/15.
 * Time on 1:59 PM
 */
@Documented
@Constraint(validatedBy = NFCReferenceConstraintValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NFCReference
{

    String message() default "Invalid NFC Reference";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
