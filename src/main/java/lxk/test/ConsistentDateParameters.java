package lxk.test;

import lxk.model.EnumValueAnn;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDate;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**@link http://www.leftso.com/blog/390.html
 * @Description:   交叉  参数   约束  开始时间--结束时间  End date must be after begin date and both must be in the future
 * @Author: lxk 
 * @Date: 2019/1/3 22:42
*/
@Constraint(validatedBy = ConsistentDateParameters.ConsistentDateParameterValidator.class)
@Target({METHOD, CONSTRUCTOR})
@Retention(RUNTIME)
@Documented
public @interface ConsistentDateParameters {

    String message() default "End date must be after begin date and both must be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    /**@SupportedValidationTarget（ValidationTarget 。参数）的注解
     *  ConsistentDateParameterValidator类是必需的。原因是因为  @ConsistentDateParameter  是在方法级别上设置的，
     *  但是约束条件应该应用于方法参数（而不是方法的返回值，我们将在下一节讨论）
     *
     *  TODO Bean验证规范建议将空值视为有效。如果null不是有效值，则应该使用@NotNull -annotation
    */
    @SupportedValidationTarget(ValidationTarget.PARAMETERS)
    class ConsistentDateParameterValidator implements ConstraintValidator<ConsistentDateParameters, Object[]> {

        private Class<?>[] groups;

        private String message;

        @Override
        public void initialize(ConsistentDateParameters consistentDateParameters) {
            //message = consistentDateParameters.message();
            //groups = consistentDateParameters.groups();
        }

        @Override
        public boolean isValid(Object[] value, ConstraintValidatorContext context) {
            if (value[0] == null || value[1] == null) {
                return true;
            }
            if (!(value[0] instanceof LocalDate) || !(value[1] instanceof LocalDate)) {
                throw new IllegalArgumentException("Illegal method signature, expected two parameters of type LocalDate.");
            }
            return ((LocalDate) value[0]).isAfter(LocalDate.now()) && ((LocalDate) value[0]).isBefore((LocalDate) value[1]);
        }
    }
}
