package com.newtonbox.mapper;

import com.newtonbox.Models.Role;
import com.newtonbox.dto.RoleDTO;
import com.newtonbox.utils.RoleEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
public class RoleMapper {


    public static RoleDTO toDTO(Role role) {
        if (role == null) return null;

        return RoleDTO.builder()
                .id(role.getId())
                .roleEnum(role.getRoleEnum().name())
                .permissions(role.getPermissionsList().stream()
                        .map(PermissionMapper::toDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) return null;
        Role role = new Role();
        role.setId(roleDTO.getId());
        try {
            role.setRoleEnum(Enum.valueOf(role.getRoleEnum().getDeclaringClass(), roleDTO.getRoleEnum()));
        } catch (IllegalArgumentException e) {
            System.err.println("Error al convertir roleEnum: " + roleDTO.getRoleEnum());
            throw new RuntimeException("Rol inválido: " + roleDTO.getRoleEnum());
        }
        if (roleDTO.getPermissions() != null) {
            role.setPermissionsList(roleDTO.getPermissions().stream()
                    .map(PermissionMapper::toEntity)
                    .collect(Collectors.toSet()));
        }
        return role;
    }
}
