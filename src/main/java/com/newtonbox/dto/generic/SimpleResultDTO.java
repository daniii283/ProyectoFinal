package com.newtonbox.dto.generic;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleResultDTO {
    private Long id;
    private String data;
}
