package lxk.annotation;

import org.springframework.context.annotation.Scope;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Scope("request")
public @interface AuthValidation {

    String actionOfMenu();

    String actionType();

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Documented
    @Scope("request")
    @interface list {

        AuthValidation[] value();
    }
}
