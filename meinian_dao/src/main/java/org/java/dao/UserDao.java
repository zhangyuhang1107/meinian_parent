package org.java.dao;

import org.java.pojo.User;

/**
 * @author zyhstart
 */
public interface UserDao {

    User findUserByUsername(String username);
}
