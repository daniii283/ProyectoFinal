package com.newtonbox.mapper;

import com.newtonbox.Models.Role;
import com.newtonbox.dto.RoleDTO;
import com.newtonbox.utils.RoleEnum;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RoleMapper {


    public static RoleDTO toDTO(Role role) {
        if (role == null) return null;

        return RoleDTO.builder()
                .id(role.getId())
                .roleEnum(role.getRoleEnum().name())
                .build();
    }

    public static Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) return null;

        Role role = new Role();
        role.setId(roleDTO.getId());

        try {
            role.setRoleEnum(RoleEnum.valueOf(roleDTO.getRoleEnum().trim()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided: " + roleDTO.getRoleEnum());
        }

        return role;
    }
}
