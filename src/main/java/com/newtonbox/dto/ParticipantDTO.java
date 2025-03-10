package com.newtonbox.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantDTO {

    private Long id;
    private UserDTO user;
    private String role;
    private ExperimentDTO experiment;
}
