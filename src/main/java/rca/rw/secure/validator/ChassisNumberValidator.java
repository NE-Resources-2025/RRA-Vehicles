package rca.rw.secure.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChassisNumberValidator implements ConstraintValidator<ValidChassis, String> {
    @Override
    public boolean isValid(String chassisNumber, ConstraintValidatorContext context) {
        return chassisNumber != null && chassisNumber.matches("^[A-HJ-NPR-Z0-9]{17}$");
    }
}