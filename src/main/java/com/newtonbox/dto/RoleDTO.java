package com.newtonbox.dto;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {

    private Long id;
    private String roleEnum;
    private Set<PermissionDTO> permissions;
}
