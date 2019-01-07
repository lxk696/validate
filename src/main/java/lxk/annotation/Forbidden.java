package lxk.annotation;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** 自定义验证规则
 有时候默认的规则可能还不够，有时候还需要自定义规则，比如屏蔽关键词验证是非常常见的一个功能，比如在发帖时帖子中不允许出现admin等关键词。


 哪个词是非法的呢？bean validation 和 hibernate validator都没有提供相应的api提供这个数据，怎么办呢？通过跟踪代码，发现一种不是特别好的方法：
 我们可以覆盖org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl实现（即复制一份代码放到我们的src中），然后覆盖buildAnnotationParameterMap方法；


 * <p>User: Zhang Kaitao 
 * <p>Date: 13-12-15 
 * <p>Version: 1.0 
 */

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
//指定验证器  
@Constraint(validatedBy = Forbidden.ForbiddenValidator.class)
@Documented
public @interface Forbidden {

    //默认错误消息  
    String message() default "{forbidden.word}";

    //分组  
    Class<?>[] groups() default {};

    //负载  
    Class<? extends Payload>[] payload() default {};

    //指定多个时使用  
    @Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Forbidden[] value();
    }

    class ForbiddenValidator implements ConstraintValidator<Forbidden, String> {

        private String[] forbiddenWords = {"admin"};

        @Override
        public void initialize(Forbidden constraintAnnotation) {
            //初始化，得到注解数据
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (StringUtils.isEmpty(value)) {
                return true;
            }

            for (String word : forbiddenWords) {
                if (value.contains(word)) {
                    ((ConstraintValidatorContextImpl)context).getConstraintDescriptor().getAttributes().put("word", word);
                    return false;//验证失败
                }
            }
            return true;
        }
    }
}
