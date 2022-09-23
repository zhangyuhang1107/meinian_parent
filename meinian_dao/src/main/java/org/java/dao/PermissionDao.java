package org.java.dao;

import org.java.pojo.Permission;

import java.util.Set;

/**
 * @author zyhstart
 */
public interface PermissionDao {

    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
