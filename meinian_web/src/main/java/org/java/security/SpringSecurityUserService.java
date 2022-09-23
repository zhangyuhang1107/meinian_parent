package org.java.security;

import com.alibaba.dubbo.config.annotation.Reference;
import org.java.pojo.Permission;
import org.java.pojo.Role;
import org.java.pojo.User;
import org.java.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zyhstart
 */
@Component("springSecurityUserService")
public class SpringSecurityUserService implements UserDetailsService {

    // 注意：此处要通过dubbo远程调用
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 远程调用用户服务，根据用户名查询用户信息
        User user = userService.findUserByUsername(username);
        if (user == null) {
            // 用户名不存在，抛出UsernameNotFoundException，框架会进行异常处理，跳转到登录页面
            return null;
        }

        // 构建权限集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 集合数据由 RoleDao 帮忙查询得到
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            // 集合数据由 PermissionDao 帮忙方法来查询得到
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                // 授权
                authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
        // 框架提供user实现userDetails接口
        return securityUser;
    }
}
