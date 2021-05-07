package com.gt.sso.service;

import com.gt.sso.vo.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User login(String username);
}
