package com.newtonbox.dto.generic;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdUsernameDTO {
    private Long id;
    private String username;
}
