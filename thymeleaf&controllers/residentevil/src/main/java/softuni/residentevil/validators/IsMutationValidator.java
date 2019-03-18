package softuni.residentevil.validators;

import softuni.residentevil.annotations.IsMutation;
import softuni.residentevil.domain.entities.Mutation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMutationValidator implements ConstraintValidator<IsMutation, Mutation> {

  @Override
  public boolean isValid(Mutation mutation, ConstraintValidatorContext constraintValidatorContext) {
    return mutation != null;
  }
}
