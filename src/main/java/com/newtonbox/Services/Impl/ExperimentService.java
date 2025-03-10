package com.newtonbox.Services.Impl;

import com.newtonbox.Models.*;
import com.newtonbox.Repository.*;
import com.newtonbox.Services.IExperimentService;
import com.newtonbox.dto.ExperimentDTO;
import com.newtonbox.mapper.CommentMapper;
import com.newtonbox.mapper.ExperimentMapper;
import com.newtonbox.mapper.ParticipantMapper;
import com.newtonbox.mapper.ResultMapper;
import com.newtonbox.utils.PermissionEnum;
import com.newtonbox.utils.RoleEnum;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("experimentService")
public class ExperimentService implements IExperimentService {

    @Autowired
    private IExperimentRepository experimentRepo;

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private IParticipantRepository participantRepo;

    @Autowired
    private ICommentRepository commentRepo;

    @Autowired
    private IResultRepository resultRepo;

    @Override
    public List<ExperimentDTO> findAll() {
        return experimentRepo.findAll().stream()
                .map(ExperimentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExperimentDTO findById(Long id) {
        Experiment experiment = experimentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Experiment not found with ID:" + id));

        return ExperimentMapper.toDTO(experiment);
    }

    // --- Personalizado--
    @Override
    public List<ExperimentDTO> findByCreatedBy(UserEntity createdBy) {
        return experimentRepo.findByCreatedBy(createdBy).stream()
                .map(ExperimentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExperimentDTO save(ExperimentDTO experimentDTO) {
       Experiment experiment = ExperimentMapper.toEntity(experimentDTO);

       Experiment savedExperiment = experimentRepo.save(experiment);

       // Participantes
       List<Participant> participants = new ArrayList<>();
       if(experimentDTO.getParticipants() != null){
           experimentDTO.getParticipants().forEach(participantDTO -> {
               Participant participant = ParticipantMapper.toEntity(participantDTO);
               participant.setExperiment(savedExperiment);
               participants.add(participantRepo.save(participant));
           });
       }

       // Comentarios
        List<Comment> comments = new ArrayList<>();
        if(experimentDTO.getComments() != null){
           experimentDTO.getComments().forEach(commentDTO -> {
               Comment comment = CommentMapper.toEntity(commentDTO);
               comment.setExperiment(savedExperiment);
               comments.add(commentRepo.save(comment));
           });
       }

       // Resultados
        List<Result> results = new ArrayList<>();
        if(experimentDTO.getResults() != null){
            experimentDTO.getResults().forEach(resultDTO -> {
                Result result = ResultMapper.toEntity(resultDTO);
                result.setExperiment(savedExperiment);
                results.add(resultRepo.save(result));
            });
        }

        savedExperiment.setParticipants(participants);
        savedExperiment.setComments(comments);
        savedExperiment.setResults(results);

        Experiment updateExperiment = experimentRepo.save(savedExperiment);
        return ExperimentMapper.toDTO(updateExperiment);
    }


    @Override
    public ExperimentDTO update(Long id, ExperimentDTO experimentDTO) {
      Experiment existingExperiment = experimentRepo.findById(id)
              .orElseThrow(() -> new RuntimeException("Experiment not found with ID: " + id));

      // Actualizar solo atributos base (sin modificar las colecciones)
        existingExperiment.setTitle(experimentDTO.getTitle());
        existingExperiment.setDescription(experimentDTO.getDescription());
        existingExperiment.setVariables(experimentDTO.getVariables());

        Experiment updatedExperiment = experimentRepo.save(existingExperiment);
        return ExperimentMapper.toDTO(updatedExperiment);
    }

    @Override
    public void delete(Long id) {
        Experiment experiment = experimentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Experiment not found with ID: " + id));
        experimentRepo.delete(experiment);
    }

    public boolean isCreator(Long experimentId, String username) {
        Experiment exp = experimentRepo.findById(experimentId)
                .orElseThrow(() -> new RuntimeException("Experiment not found with ID: " + experimentId));
        return exp.getCreatedBy().getUsername().equals(username);
    }

    public boolean isUserAllowed(Long experimentId, String username) {
       Experiment exp  = experimentRepo.findById(experimentId)
               .orElseThrow(() -> new RuntimeException("Experiment not found with ID: " + experimentId));
       boolean isCreator = exp.getCreatedBy().getUsername().equals(username);
       boolean isParticipant = exp.getParticipants().stream()
               .anyMatch(p -> p.getUser().getUsername().equals(username));
       return isCreator || isParticipant;
    }


    // Métodos auxiliares
    private RoleEnum assignRoleBasedOnUser(UserEntity user) {
        if (user == null) {
            throw new RuntimeException("Error: El usuario recibido es NULL.");
        }

        System.out.println("Asignando rol para usuario: " + user.getUsername());

        // Forzar la carga de roles manualmente
        user.getRoles().size();

        System.out.println("Roles del usuario: " + user.getRoles());

        if (user.getRoles().isEmpty()) {
            throw new RuntimeException("El usuario " + user.getUsername() + " no tiene roles asignados.");
        }

        // Obtener el primer rol del usuario
        Role role = user.getRoles().iterator().next();

        // Devolver el RoleEnum correspondiente
        return role.getRoleEnum();
    }
}
