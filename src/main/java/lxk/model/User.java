package lxk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lxk.handler.ErrorEmailSender;
import lxk.handler.Severity;
import lxk.test.ConsistentDateParameters;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.executable.ExecutableValidator;
import javax.validation.groups.ConvertGroup;
import java.io.Serializable;
import java.lang.reflect.Method;
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
public class User implements Serializable {

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

    /**
     * 昵称
     *  TODO payload 用于放入元信息数据，可用于标记验证错误的级别，可以更加不同的错误级别做不同的处理
     */
    @Length(min = 5, max = 20, message = "用户名长度必须在5到20之间", groups = {First.class}, payload = {Severity.Info.class})
    @Pattern(regexp = "[a-zA-Z]{1,6}", message = "用户名长度必须在1到20之间的字母", groups = {Second.class, CreateGroup.class}, payload = {Severity.Error.class, ErrorEmailSender.class})
    @NotBlank(groups = {Second.class, CreateGroup.class})
    private String nickname;
    /**
     * 电话
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "{custom.phone.invalid}", groups = CreateGroup.class)
    private String phone;



    /**
     * 级联验证只要在相应的字段上加@Valid即可，会进行级联验证；
     * @ConvertGroup 的作用是当验证 User 的分组是First时，那么验证books的分组是Second，即分组验证的转换。
    */
    //@Valid //TODO
    @ConvertGroup(from = First.class, to = Second.class)
    private List<Book> books;



    // TODO 与单参数约束相反，交叉参数约束在方法或构造函数中声明：
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
