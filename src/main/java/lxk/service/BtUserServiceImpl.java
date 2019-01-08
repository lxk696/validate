package lxk.service;

import lombok.extern.slf4j.Slf4j;
import lxk.model.BtUser;
import lxk.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 刘雄康
 * @version v1.0
 * @description book
 * @date 2018年12月06日
 */
@Service
@Slf4j
public class BtUserServiceImpl implements BtUserService {
    @Override
    public BtUser insertAndReturn(BtUser user) {
        return user;
    }

    @Override
    public User insertAndReturn(User user, String testEmail, Date testDate, Integer testRange) {
        return user;
    }

    @Override
    public void changePassword(String password, String confirmation) {
        log.info("test  CrossParameter the password and confirmation!");
    }
}
