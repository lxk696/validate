package lxk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lxk.handler.ErrorEmailSender;
import lxk.handler.Severity;
import lxk.test.AuthValidation;
import lxk.test.ConsistentDateParameters;
import lxk.test.Forbidden;
import lxk.test.PropertyScriptAssert2;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @desc 用户PO
 *
 * @author zhumaer
 * @since 6/15/2017 2:48 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@CheckPassword.list({
//        @CheckPassword(),
//        @CheckPassword()
//})
//@CheckPassword()
//@ScriptAssert(script = "_this.pwd==_this.confirmation", lang = "javascript", alias = "_this", message = "{password.confirmation.error}")//TODO 这样全局异常捕获不到错误字段
@PropertyScriptAssert2(property = "confirmation", script = "_this.password==_this.confirmation", lang = "javascript", alias = "_this", message = "{password.confirmation.error}")
public class User implements Serializable {
    //@AuthValidation.list({
    //        @AuthValidation(actionOfMenu="",actionType=""),
    //        @AuthValidation(actionOfMenu="",actionType="")
    //})
    //@Configuration
    //@ComponentScan({ "org.baeldung.javaxval.methodvalidation.model" })
    //public class MethodValidationConfig {
    //    @Bean
    //    public MethodValidationPostProcessor methodValidationPostProcessor() {
    //        return new MethodValidationPostProcessor();
    //    }
    //}

    //程序化验证  !=  @Validated 为 aop方法拦截技术
    // 对于  独立Java应用程序中的手动方法验证，我们可以使用  javax.validation.executable.ExecutableValidator接口。
    //我们可以使用以下代码检索实例：
    //ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    //ExecutableValidator executableValidator = factory.getValidator().forExecutables();
    //ExecutableValidator提供了四种方法：
    //用于方法验证的validateParameters（）和validateReturnValue（）
    //用于构造函数验证的validateConstructorParameters（）和validateConstructorReturnValue（）
    //验证我们的第一个方法createReservation（）的参数如下所示：
    //ReservationManagement object = new ReservationManagement();
    //Method method = ReservationManagement.class.getMethod("createReservation", LocalDate.class, int.class, Customer.class);
    //Object[] parameterValues = { LocalDate.now(), 0, null };
    //Set<ConstraintViolation<ReservationManagement>> violations = executableValidator.validateParameters(object, method, parameterValues);
    //注意：官方文档不鼓励直接从应用程序代码调用此接口，而是通过方法拦截技术（如AOP或代理）来使用它。

    private static final long serialVersionUID = 2594274431751408585L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 登录密码
     */
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]{5,19}$", message = "{custom.pwd.invalid}")
    private String pwd;

    private String confirmation;

    /**
     * 昵称
     *  TODO payload 用于放入元信息数据，可用于标记验证错误的级别，可以更加不同的错误级别做不同的处理
     */
    @Length(min = 5, max = 21, message = "{custom.nickname.length.invalid}", groups = { First.class}, payload = {Severity.Info.class})
    @Pattern(regexp = "[a-zA-Z]{1,6}", message = "用户名长度必须在1到20之间的字母", groups = { Second.class, CreateGroup.class}, payload = {Severity.Error.class, ErrorEmailSender.class})
    @NotBlank
    @Forbidden(message = "{forbidden.word2}")//  在 Forbidden 里已经写了 (message = "{forbidden.word}")
    private String nickname;


    /**
     * 电话
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "{custom.phone.invalid}", groups = CreateGroup.class)
    private String phone;



    /**
     *   FIXME  怎么让只有 update + first 同时满足时才做约束校验（约束后的 groups = {First.class，Second.class} 只要在@Validated里满足一个就会触发校验 ）。只能再创建个updateFirst分组了？
     *   TODO 级联验证只要在相应的字段上加@Valid即可，会进行级联验证；  无 @ConvertGroup时
     *   1   addUser(@Validated) 没有使用分组时,此时只能校验没有分组的默认约束
     *   2   addUser(@Validated({CreateGroup.class, SequenceGroup.class})  使用分组，CreateGroup继承了默认分组。此时books只会校验  默认约束+ CreateGroup+ SequenceGroup 约束。！但 默认约束会 和  first约束同时校验！
     */
    @Valid
   // @ConvertGroup(from = CreateGroup.class, to = UpdateGroup.class) //TODO  @ConvertGroup 的作用是当验证 User 的分组是CreateGroup时，那么验证books的分组是UpdateGroup，即分组验证的转换。
    private List<Book> books;



     // TODO 书名books.bookName
    //@NotEmpty(message = "书名不能为空")
    //@Pattern(regexp = "[a-zA-Z]{1,6}", message = "bookName长度需为1到6个字符之间的字母", groups = Second.class) //@Pattern 只对字符串有用
    //@Length(min = 5, max = 20, message = "bookName长度必须在5到20之间", groups = {First.class})
    //private String bookName;



    // TODO 与单参数约束相反，交叉参数约束在方法或构造函数中声明：
    @AuthValidation.list({
            @AuthValidation(actionOfMenu="",actionType=""),
            @AuthValidation(actionOfMenu="",actionType="")
    })
    //@CheckPassword.list({
    //        @CheckPassword(),
    //        @CheckPassword()
    //})
    @ConsistentDateParameters
    public void createReservation(LocalDate begin, LocalDate end, User user) {
        // ...
    }


    /**
     * 对于allUsers（），以下约束条件适用：
     *         首先，返回的列表不能为空，且必须至少有一个条目
     *         此外，该列表不能包含空条目
     */
    @NotNull
    @Size(min = 1)
    //public List<@NotNull User> allUsers() {
    //public @NotNull @Size(min = 1)List<User> allUsers() {
    public List<User> allUsers() {
        return null;
    }


    /**
     * 状态 {@link StatusEnum}
     */
    @EnumValueAnn(enumClass = StatusEnum.class, enumMethod = "isValidCode", groups = CreateGroup.class)
    private Integer status;










    /**
     * 头像
     */
    @Length(min = 0, max = 256, groups = CreateGroup.class)
    private String img;

    /**
     * 性别 {@link} 0 男 1 女
     */
    @Range(min = 0, max = 1, groups = CreateGroup.class)
    private Integer gender;

    /**
     * 最新的登录时间
     */
    private Date latestLoginTime;

    /**
     * 最新的登录IP
     */
    private String latestLoginIp;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;





    /**
     * @desc 账号状态枚举
     *
     * @author zhumaer
     * @since 6/15/2017 2:48 PM
     */
    public enum StatusEnum {
        NORMAL(0, "正常"), SUSPENDED(1, "停用"), DELETED(2, "已删除");

        private Integer code;

        private String desc;

        StatusEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static boolean isValidCode(Integer code) {

            if (code == null) {
                return false;
            }

            for (StatusEnum status : StatusEnum.values()) {
                if (status.getCode().equals(code)) {
                    return true;
                }
            }
            return false;
        }

    }

}
