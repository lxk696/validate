package lxk.model;

import javax.validation.GroupSequence;

/**
 * @author 刘雄康
 * @version v1.0
 * @description
 * @date 2018年12月06日
 */
@GroupSequence({First.class, Second.class, User.class})
public interface SequenceGroup {
}
