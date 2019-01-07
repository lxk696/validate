package lxk.annotation;

import lxk.model.CreateGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD})
@Retention(RUNTIME)
@Documented
@NotNull(message = "Composition--{user.name.null}")
@Length(min = 5, max = 20, message = "Composition--{user.name.length.illegal}")
//@Pattern(regexp = "[a-zA-Z]{5,20}", message = "{user.name.length.illegal}")
@Pattern(regexp = "^1[3-9]\\d{10}$", message = "Composition--{custom.phone.invalid}", groups = CreateGroup.class)
@Constraint(validatedBy = {

})
public @interface Composition {  
    String message() default "";  
    Class<?>[] groups() default { };  
    Class<? extends Payload>[] payload() default { };
}  