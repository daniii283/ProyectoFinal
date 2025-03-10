package com.newtonbox.dto.generic;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleCommentDTO {
    private Long id;
    private String content;
    private IdUsernameDTO user;
    private LocalDateTime timestamp;
}
