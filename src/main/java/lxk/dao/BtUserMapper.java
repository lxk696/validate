package lxk.dao;

import lxk.model.BtUser;

public interface BtUserMapper {
    int insert(BtUser record);

    int insertSelective(BtUser record);
}