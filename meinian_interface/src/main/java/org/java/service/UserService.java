package org.java.service;

import org.java.pojo.User;

/**
 * @author zyhstart
 */
public interface UserService {

    User findUserByUsername(String username);
}
