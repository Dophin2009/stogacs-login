package net.edt.web.validation.validator;

import net.edt.web.validation.constraint.MatchingFields;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

public class MatchingFieldsValidator implements ConstraintValidator<MatchingFields, Object> {

    private String first, second;

    @Override
    public void initialize(MatchingFields annotation) {
        first = annotation.first();
        second = annotation.second();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;
        try {
            final Object firstField = BeanUtils.getProperty(o, first);
            final Object secondField = BeanUtils.getProperty(o, second);

            valid = firstField == null && secondField == null || firstField != null && firstField.equals(secondField);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }

        return valid;
    }

}
