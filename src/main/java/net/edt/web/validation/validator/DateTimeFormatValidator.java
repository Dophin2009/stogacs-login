package net.edt.web.validation.validator;

import net.edt.web.validation.constraint.DateTimeType;
import net.edt.web.validation.constraint.DateTimeFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatValidator implements ConstraintValidator<DateTimeFormat, String> {

    private String format;
    private DateTimeType dateTimeType;

    @Override
    public void initialize(DateTimeFormat annotation) {
        format = annotation.format();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        try {
            if (dateTimeType == DateTimeType.DATE_TIME) {
                LocalDateTime.parse(s, formatter);
            } else {
                LocalDate.parse(s, formatter);
            }
            return true;
        } catch (DateTimeException ex) {
            return false;
        }
    }

}
