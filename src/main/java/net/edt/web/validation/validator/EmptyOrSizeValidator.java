package net.edt.web.validation.validator;

import net.edt.web.validation.constraint.EmptyOrSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmptyOrSizeValidator implements ConstraintValidator<EmptyOrSize, CharSequence> {

    private int min;
    private int max;

    @Override
    public void initialize(EmptyOrSize annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
        if (charSequence == null || charSequence.length() == 0) {
            return true;
        }

        int length = charSequence.length();
        return length >= min && length <= max;
    }

}
