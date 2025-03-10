package com.newtonbox.mapper;

import com.newtonbox.Models.Participant;
import com.newtonbox.dto.ParticipantDTO;
import com.newtonbox.utils.PermissionEnum;
import com.newtonbox.utils.RoleEnum;

public class ParticipantMapper {

    public static ParticipantDTO toDTO(Participant participant){
        if (participant == null) return null;

        return ParticipantDTO.builder()
                .id(participant.getId())
                .user(UserMapper.toDTO(participant.getUser()))
                .role(participant.getRole().name())
                .build();
    }

    public static Participant toEntity(ParticipantDTO participantDTO){
        if (participantDTO == null) return null;

        System.out.println("🔍 Valor recibido en role: " + participantDTO.getRole());

        Participant participant = new Participant();
        participant.setId(participantDTO.getId());
        participant.setUser(UserMapper.toEntity(participantDTO.getUser()));

        // Manejo de excepciones seguro al convertir `String` a `Enum`
        try {
            if (participantDTO.getRole() != null) {
                participant.setRole(RoleEnum.valueOf(participantDTO.getRole().trim())); // O RoleEnum si aplica
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error al convertir role: " + participantDTO.getRole());
            throw new RuntimeException("Rol inválido: " + participantDTO.getRole());
        }

        if (participantDTO.getExperiment() != null){
            participant.setExperiment(ExperimentMapper.toEntity(participantDTO.getExperiment()));
        }else{
            throw new RuntimeException("The experiment cannot be null");
        }

        return participant;
    }
}
