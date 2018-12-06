package lxk.service;

import lxk.model.BtUser;
import lxk.model.User;

/**
 * @author 刘雄康
 * @version v1.0
 * @description book
 * @date 2018年12月06日
 */
public interface BtUserService {
    BtUser insertAndReturn(BtUser user);
    User insertAndReturn(User user);
}
