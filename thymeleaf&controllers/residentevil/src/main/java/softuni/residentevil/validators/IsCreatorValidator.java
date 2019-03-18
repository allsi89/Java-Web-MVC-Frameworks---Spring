package softuni.residentevil.validators;

import softuni.residentevil.annotations.IsCreator;
import softuni.residentevil.domain.entities.Creator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsCreatorValidator implements ConstraintValidator<IsCreator, Creator> {

  @Override
  public boolean isValid(Creator creator, ConstraintValidatorContext constraintValidatorContext) {
    return creator != null;
  }

}
