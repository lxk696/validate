package lxk.service;

import lxk.model.BtUser;
import lxk.model.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

/**
 * @author 刘雄康
 * @version v1.0
 * @description book
 * @date 2018年12月06日
 */
@Service
@Validated
public class BtUserServiceImpl implements BtUserService {
    @Override
    public BtUser insertAndReturn(BtUser user) {
        return user;
    }

    @Override
    public User insertAndReturn(User user, String testEmail, Date testDate, Integer testRange) {
        return user;
    }
}
