package net.edt.web.validation.constraint;

import net.edt.web.validation.validator.MatchingFieldsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MatchingFieldsValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchingFields {

    String first();

    String second();

    String message() default "fields do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
