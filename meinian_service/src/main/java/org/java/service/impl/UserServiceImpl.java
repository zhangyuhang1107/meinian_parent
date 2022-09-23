package org.java.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.java.dao.UserDao;
import org.java.pojo.User;
import org.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zyhstart
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }
}
