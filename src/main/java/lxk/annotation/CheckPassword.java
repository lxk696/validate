package lxk.annotation;

import lxk.model.User;
import org.hibernate.validator.constraints.ScriptAssert;
import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

/** 类级别验证器
 * 其中我们通过disableDefaultConstraintViolation禁用默认的约束；然后通过buildConstraintViolationWithTemplate(消息模板)/addPropertyNode(所属属性)/addConstraintViolation定义我们自己的约束。
 * <p>User: Zhang Kaitao 
 * <p>Date: 13-12-15 
 * <p>Version: 1.0 
 */  
  
@Target({ TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
//指定验证器
@Constraint(validatedBy = CheckPassword.CheckPasswordValidator.class)
@Documented
@ScriptAssert(script = "_this.password==_this.confirmation", lang = "javascript", alias = "_this", message = "{password.confirmation.error}")
public @interface CheckPassword {
  
    //默认错误消息  
    String message() default "";  
  
    //分组  
    Class<?>[] groups() default { };  
  
    //负载  
    Class<? extends Payload>[] payload() default { };  
  
    //指定多个时使用  
    //@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
    //@Retention(RUNTIME)
    //@Documented
    @Target({ TYPE, ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Documented
    @interface list {
        CheckPassword[] value();
    }

    class CheckPasswordValidator implements ConstraintValidator<CheckPassword, User> {

        @Override
        public void initialize(CheckPassword constraintAnnotation) {
        }

        @Override
        public boolean isValid(User user, ConstraintValidatorContext context) {
            if(user == null) {
                return true;
            }

            //没有填密码
            if(!StringUtils.hasText(user.getPwd())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{password.null}")
                        .addPropertyNode("password")
                        .addConstraintViolation();
                return false;
            }

            if(!StringUtils.hasText(user.getConfirmation())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{password.confirmation.null}")
                        .addPropertyNode("confirmation")
                        .addConstraintViolation();
                return false;
            }

            //两次密码不一样
            if (!user.getPwd().trim().equals(user.getConfirmation().trim())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{password.confirmation.error}")
                        .addPropertyNode("confirmation")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }
    }
}  