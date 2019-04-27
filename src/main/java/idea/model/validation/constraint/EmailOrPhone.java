package idea.model.validation.constraint;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.CompositionType;

@ConstraintComposition(CompositionType.OR)
@Pattern(regexp="^(?:(?:\\(?(?:00|\\+)([1-4]\\d\\d|[1-9]\\d?)\\)?)?[\\-\\.\\ \\\\\\/]?)?((?:\\(?\\d{1,}\\)?[\\-\\.\\ \\\\\\/]?){0,})(?:[\\-\\.\\ \\\\\\/]?(?:#|ext\\.?|extension|x)[\\-\\.\\ \\\\\\/]?(\\d+))?$", message="{invalid.phonenumber}")
@Email
@ReportAsSingleViolation
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
public @interface EmailOrPhone {
  String message() default "{value} is not a valid email or phone number";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };
}
