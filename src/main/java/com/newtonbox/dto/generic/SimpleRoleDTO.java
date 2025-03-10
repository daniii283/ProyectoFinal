package com.newtonbox.dto.generic;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleRoleDTO {
    private Long id;
    private String roleEnum;
}
