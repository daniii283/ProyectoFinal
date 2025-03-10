package com.newtonbox.mapper;

import com.newtonbox.Models.Participant;
import com.newtonbox.Models.Permission;
import com.newtonbox.dto.ParticipantDTO;
import com.newtonbox.dto.PermissionDTO;
import com.newtonbox.utils.PermissionEnum;

public class PermissionMapper {

    public static PermissionDTO toDTO(Permission permission){
        if(permission == null) return null;

        return PermissionDTO.builder()
                .id(permission.getId())
                .permissionEnum(permission.getPermissionEnum().name())
                .build();
    }
    public static Permission toEntity(PermissionDTO permissionDTO) {
        if (permissionDTO == null) return null;
        Permission permission = new Permission();
        permission.setId(permissionDTO.getId());
        try {
            permission.setPermissionEnum(Enum.valueOf(permission.getPermissionEnum().getDeclaringClass(), permissionDTO.getPermissionEnum()));
        } catch (IllegalArgumentException e) {
            System.err.println("Error al convertir permissionEnum: " + permissionDTO.getPermissionEnum());
            throw new RuntimeException("Permission inválida: " + permissionDTO.getPermissionEnum());
        }
        return permission;
    }
}
