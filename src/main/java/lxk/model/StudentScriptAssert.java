package lxk.model;

import org.hibernate.validator.constraints.ScriptAssert;

//通过script 属性指定进行校验的方法，传递校验的参数，
@ScriptAssert(lang = "javascript", script = "com.learn.validate.domain.Student.checkParams(_this.name,_this.age,_this.classes)", message = "have error msg: StudentScriptAssert is wrong")
public class StudentScriptAssert {

    private String name;

    private int age;

    private String classess;

    //注意进行校验的方法要写成静态方法，否则会出现   
    //TypeError: xxx is not a function 的错误  
    public static boolean checkParams(String name, int age, String classes) {
        if (name != null && age > 8 & classes != null) {
            return true;
        } else {
            return false;
        }
    }
}
