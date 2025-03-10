package com.newtonbox.Services.Impl;

import com.newtonbox.Models.Experiment;
import com.newtonbox.Models.Participant;
import com.newtonbox.Models.Permission;
import com.newtonbox.Models.UserEntity;
import com.newtonbox.Repository.IExperimentRepository;
import com.newtonbox.Repository.IParticipantRepository;
import com.newtonbox.Repository.IUserRepository;
import com.newtonbox.Services.IParticipantService;
import com.newtonbox.dto.ParticipantDTO;
import com.newtonbox.mapper.ParticipantMapper;
import com.newtonbox.utils.PermissionEnum;
import com.newtonbox.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipantService implements IParticipantService {

    @Autowired
    private IParticipantRepository participantRepo;

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private IExperimentRepository experimentRepo;

    @Override
    public List<ParticipantDTO> findAll() {
        return participantRepo.findAll().stream()
                .map(ParticipantMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipantDTO findById(Long id) {
        Participant participant = participantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found with ID:" + id));
        return ParticipantMapper.toDTO(participant);
    }

    // --- Personalizado--
    @Override
    public List<ParticipantDTO> findByExperiment(Long experimentId) {
        Experiment experiment = experimentRepo.findById(experimentId)
                .orElseThrow(() -> new RuntimeException("Experiment not found with ID: " + experimentId));
        return participantRepo.findByExperiment(experiment).stream()
                .map(ParticipantMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --- Personalizado--
    @Override
    public List<ParticipantDTO> findByUser(Long userId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Participants not found with ID: " + userId));
        return participantRepo.findByUser(user).stream()
                .map(ParticipantMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipantDTO save(ParticipantDTO participantDTO) {
        // Convertir el DTO a entidad
        Participant participant = ParticipantMapper.toEntity(participantDTO);
        if(participant.getUser() == null || participant.getUser().getId() == null){
            throw new RuntimeException("The participant's username cannot be null.");
        }

        // Verificar que el usuario existe
        UserEntity user = userRepo.findById(participant.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + participant.getUser().getId()));

        Participant saved = participantRepo.save(participant);
        return ParticipantMapper.toDTO(saved);
    }



        @Override
    public ParticipantDTO update(Long id, ParticipantDTO participantDTO) {
        Participant existingParticipant = participantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found with ID: " + id ));

        if(participantDTO.getRole() != null){
            try{
                existingParticipant.setRole(RoleEnum.valueOf(participantDTO.getRole()));
            }catch (IllegalArgumentException e){
                throw new RuntimeException("Invalid role provided: " + participantDTO.getRole());
            }
        }

        Participant updated = participantRepo.save(existingParticipant);
        return ParticipantMapper.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        Participant existing = participantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found with ID: " + id));
        participantRepo.delete(existing);
    }
}
