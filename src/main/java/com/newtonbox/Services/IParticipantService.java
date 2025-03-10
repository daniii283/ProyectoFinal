package com.newtonbox.Services;

import com.newtonbox.dto.ParticipantDTO;

import java.util.List;

public interface IParticipantService {

    List<ParticipantDTO> findAll();
    ParticipantDTO findById(Long id);
    List<ParticipantDTO> findByExperiment(Long experimentId); // Personalizado
    List<ParticipantDTO> findByUser(Long userId); // Personalizado
    ParticipantDTO save(ParticipantDTO participantDTO);
    ParticipantDTO update(Long id, ParticipantDTO participantDTO);
    void delete(Long id);
}
