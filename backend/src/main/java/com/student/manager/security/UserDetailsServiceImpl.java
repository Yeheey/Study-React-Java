package com.student.manager.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.student.manager.entity.SysMenu;
import com.student.manager.entity.SysRole;
import com.student.manager.entity.SysUser;
import com.student.manager.mapper.SysMenuMapper;
import com.student.manager.mapper.SysRoleMapper;
import com.student.manager.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysMenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        SecurityUser securityUser = new SecurityUser();
        securityUser.setId(user.getId());
        securityUser.setUsername(user.getUsername());
        securityUser.setPassword(user.getPassword());
        securityUser.setStatus(user.getStatus());

        List<SysRole> roles = roleMapper.selectRolesByUserId(user.getId());
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleKey()));
            List<SysMenu> menus = menuMapper.selectMenusByRoleId(role.getId());
            for (SysMenu menu : menus) {
                if (menu.getPerms() != null && !menu.getPerms().isEmpty()) {
                    permissions.add(menu.getPerms());
                    authorities.add(new SimpleGrantedAuthority(menu.getPerms()));
                }
            }
        }

        securityUser.setAuthorities(authorities);
        securityUser.setPermissions(permissions);

        return securityUser;
    }
}
