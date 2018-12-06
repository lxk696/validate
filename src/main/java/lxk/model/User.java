package lxk.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * @desc 用户PO
 *
 * @author zhumaer
 * @since 6/15/2017 2:48 PM
 */
public class User implements Serializable {

    private static final long serialVersionUID = 2594274431751408585L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 登录密码
     */
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]{5,19}$", groups = CreateGroup.class, message = "{custom.pwd.invalid}")
    private String pwd;

    /**
     * 昵称
     */
    @NotBlank
    @Length(min = 1, max = 64, groups = CreateGroup.class)
    private String nickname;

    /**
     * 头像
     */
    @Length(min = 0, max = 256, groups = CreateGroup.class)
    private String img;

    /**
     * 电话
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "{custom.phone.invalid}", groups = CreateGroup.class)
    private String phone;

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
     * 状态 {@link StatusEnum}
     */
    @EnumValueAnn(enumClass = StatusEnum.class, enumMethod = "isValidCode", groups = CreateGroup.class)
    private Integer status;

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
        NORMAL(0, "正常"),
        SUSPENDED(1, "停用"),
        DELETED(2, "已删除");

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


    //省略getter、setter方法（这里你可以引入lombok来简化类的getter、setter）。

}
