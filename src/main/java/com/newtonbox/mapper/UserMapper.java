package com.newtonbox.mapper;

import com.newtonbox.Models.Role;
import com.newtonbox.Models.UserEntity;
import com.newtonbox.dto.UserDTO;
import com.newtonbox.dto.RoleDTO;
import com.newtonbox.utils.RoleEnum;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(UserEntity user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles() != null ?
                        user.getRoles().stream()
                        .map(role -> new RoleDTO(role.getId(), role.getRoleEnum().name()))
                        .collect(Collectors.toSet())
                        : null)
                .build();
    }

    public static UserEntity toEntity(UserDTO userDTO) {
        if (userDTO == null) return null;

        UserEntity user = new UserEntity();
        user.setId(userDTO.getId());

        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }

        if (userDTO.getPassword() != null){
            user.setPassword(userDTO.getPassword());
        }

        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()){
            user.setRoles(userDTO.getRoles().stream()
                    .map(roleDTO -> {
                        Role role = new Role();
                        role.setId(roleDTO.getId());
                        role.setRoleEnum(RoleEnum.valueOf(roleDTO.getRoleEnum().trim()));
                        return role;

                    })
                    .collect(Collectors.toSet()));
        }
        return user;
    }
}
