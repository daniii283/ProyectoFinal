package com.newtonbox.mapper;

import com.newtonbox.Models.UserEntity;
import com.newtonbox.dto.UserDTO;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO (UserEntity user){
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(RoleMapper::toDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static UserEntity toEntity(UserDTO userDTO){
        if (userDTO == null) return null;

        UserEntity user = new UserEntity();

        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        if(userDTO.getRoles() != null){
            user.setRoles(userDTO.getRoles().stream()
                    .map(RoleMapper::toEntity)
                    .collect(Collectors.toSet()));
        }
        return user;

    }
}
