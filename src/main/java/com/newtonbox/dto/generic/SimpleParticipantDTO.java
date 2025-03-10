package com.newtonbox.dto.generic;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleParticipantDTO {
    private Long id;
    private IdUsernameDTO user;
    private String role;
}
