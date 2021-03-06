package softuni.residentevil.annotations;


import softuni.residentevil.validators.IsCreatorValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsCreatorValidator.class)
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
public @interface IsCreator {

  String message() default "Invalid Value. Creator value must be 'Corp' or 'corp'.";

  Class<? extends Enum<?>> enumClass();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
