package softuni.residentevil.validators;

import softuni.residentevil.annotations.IsMagnitude;
import softuni.residentevil.domain.entities.Magnitude;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMagnitudeValidator implements ConstraintValidator<IsMagnitude, Magnitude> {

  @Override
  public boolean isValid(Magnitude magnitude, ConstraintValidatorContext constraintValidatorContext) {
    return magnitude != null;
  }
}
