package org.java.dao;

import org.java.pojo.Role;

import java.util.Set;

/**
 * @author zyhstart
 */
public interface RoleDao {

    Set<Role> findRolesByUserId(Integer userId);
}
