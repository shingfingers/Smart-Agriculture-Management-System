package com.itbaizhan.farm_system.security;

import com.itbaizhan.farm_system.entity.Permission;
import com.itbaizhan.farm_system.entity.User;
import com.itbaizhan.farm_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.认证
        User user = userService.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        // 2.授权
        List<Permission> permissions = userService.findAllPermission(username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (permissions != null && !permissions.isEmpty() && !permissions.stream().allMatch(Objects::isNull)){
            for (Permission permission : permissions) {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getComponent()));
            }
        }

        // 3.封装为UserDetails对象
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .disabled(user.getStatus().equals("1")) // 如果 status=1 表示禁用
                .authorities(grantedAuthorities)
                .build();

        // 4.返回封装好的UserDetails对象
        return userDetails;
    }
}
