package lxk.test;

import java.lang.annotation.*;

/**
 * @author 刘雄康
 * @version v1.0
 * @description
 * @date 2019年01月03日
 */
@Target(ElementType.TYPE) // 使用范围在类、接口和枚举
@Retention(RetentionPolicy.RUNTIME) // 生命周期在运行时期，可以进行反射操作
@Repeatable(MyRepeatAnnotationContainer.class) // 重复注解，需要指定注解容器
public @interface MyRepeatAnnotation {
    String value();// 定义一个属性值
}