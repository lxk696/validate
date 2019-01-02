package lxk.service;

import lxk.model.BtUser;
import lxk.model.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * @author 刘雄康
 * @version v1.0
 * @description book
 * @date 2018年12月06日
 */
public interface BtUserService {
    BtUser insertAndReturn(BtUser user);

    // User insertAndReturn(User user);

    // User insertAndReturn(
    //         User user,
    //         String testEmail,
    //         Date testDate,
    //         Integer testRange);

    @NotNull
    User insertAndReturn(
            @NotNull User user,
            @NotBlank(message = "testEmail为空") @Email(message = "testEmail错误") String testEmail,
            @NotNull(message = "testDate为空") @Past(message = "testDate错误") Date testDate,
            @NotNull(message = "testRange为空") @Range(message = "testRange错误", min = 1, max = 10) Integer testRange);

}

