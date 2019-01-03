package lxk.test;

import java.util.Arrays;

@MyRepeatAnnotation("Hello")
@MyRepeatAnnotation("World")
public class MyRepeatAnnotationTest {

    public static void main(String[] args) {
        Class<MyRepeatAnnotationTest> clazz = MyRepeatAnnotationTest.class;
        // 根据注解类型获取Main类上的所有注解
        MyRepeatAnnotation[] annotations = clazz.getAnnotationsByType(MyRepeatAnnotation.class);
        // 输出找到的所有注解
        Arrays.stream(annotations).forEach(System.out::println);
    }
}
