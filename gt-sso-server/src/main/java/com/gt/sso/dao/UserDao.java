package com.gt.sso.dao;

import com.gt.sso.vo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends MyMapper<User> {

    User selectByUsername(String username);
}
