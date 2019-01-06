package lxk.service;

import lxk.model.BtUser;
import lxk.model.User;
import lxk.test.CrossParameter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * @author 刘雄康
 * @version v1.0
 * @description book
 * @date 2018年12月06日
 */
@Validated
public interface BtUserService {
    public BtUser insertAndReturn(BtUser user);

    // User insertAndReturn(User user);

    // User insertAndReturn(
    //         User user,
    //         String testEmail,
    //         Date testDate,
    //         Integer testRange);

    @NotNull
    public  User insertAndReturn(
            @NotNull User user,
            @NotBlank(message = "testEmail为空") @Email(message = "{test.email.error}") String testEmail,
            @NotNull(message = "testDate为空") @Past(message = "testDate错误") Date testDate,
            @NotNull(message = "testRange为空") @Range(message = "testRange错误", min = 1, max = 10) Integer testRange);


    @CrossParameter
    public void changePassword(String password, String confirmation);

}

