package lxk.test;


import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CrossParameter.CrossParameterValidator.class)
@Target({METHOD, CONSTRUCTOR, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented

public @interface CrossParameter {

    String message() default "{password.confirmation.error.crossParameter}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    @SupportedValidationTarget(ValidationTarget.PARAMETERS) //    表示验证参数； value将是参数列表。
    class CrossParameterValidator implements ConstraintValidator<CrossParameter, Object[]> {

        @Override
        public void initialize(CrossParameter constraintAnnotation) {
            ;
        }

        @Override
        public boolean isValid(Object[] value, ConstraintValidatorContext context) {
            if (value == null || value.length != 2) {
                throw new IllegalArgumentException("must have two args");
            }
            if (value[0] == null || value[1] == null) {
                return true;
            }
            if (value[0].equals(value[1])) {
                return true;
            }
            return false;
        }
    }
}
