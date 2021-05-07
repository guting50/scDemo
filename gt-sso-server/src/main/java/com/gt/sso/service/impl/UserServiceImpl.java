package com.gt.sso.service.impl;

import com.gt.sso.dao.UserDao;
import com.gt.sso.service.UserService;
import com.gt.sso.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    public User login(String username) {
        return userDao.selectByUsername(username);
    }
}
