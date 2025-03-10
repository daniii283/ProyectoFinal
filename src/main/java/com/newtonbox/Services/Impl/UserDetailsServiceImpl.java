package com.newtonbox.Services.Impl;

import com.newtonbox.Models.UserEntity;
import com.newtonbox.Repository.IUserRepository;
import com.newtonbox.Security.CustomeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Verificar los roles que tiene asignados
        user.getRoles().forEach(role -> {
            String roleName = "ROLE_" + role.getRoleEnum().name();
            authorityList.add(new SimpleGrantedAuthority(roleName));

            // Verificar los permisos asociados a los roles
            role.getPermissionsList().forEach(permission -> {
                authorityList.add(new SimpleGrantedAuthority(permission.getPermissionEnum().name()));
            });
        });

        return new CustomeUserDetails(user.getId(),user.getUsername(), user.getPassword(),
                user.isEnabled(),
                user.isAccountNoExpired(),
                user.isCredentialNoExpired(),
                user.isAccountNoLocked(),
                authorityList);
    }


}
