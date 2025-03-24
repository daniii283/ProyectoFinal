package com.newtonbox.Services.Impl;

import com.newtonbox.Models.Comment;
import com.newtonbox.Models.Experiment;
import com.newtonbox.Models.UserEntity;
import com.newtonbox.Repository.ICommentRepository;
import com.newtonbox.Repository.IExperimentRepository;
import com.newtonbox.Repository.IUserRepository;
import com.newtonbox.Security.CustomUserDetails;
import com.newtonbox.Services.ICommentService;
import com.newtonbox.dto.CommentDTO;
import com.newtonbox.dto.ExperimentDTO;
import com.newtonbox.mapper.CommentMapper;
import com.newtonbox.mapper.ExperimentMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private ICommentRepository commentRepo;

    @Autowired
    private IExperimentRepository experimentRepo;

    @Autowired
    private IUserRepository userRepo;

    @Override
    public List<CommentDTO> findAll() {
        return commentRepo.findAll().stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO findById(Long id) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));
        return CommentMapper.toDTO(comment);
    }

    // --- Personalizado--
    @Override
    public List<CommentDTO> findByExperiment(ExperimentDTO experimentDTO) {
        Experiment experiment = ExperimentMapper.toEntity(experimentDTO);
        return commentRepo.findByExperiment(experiment).stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO save(CommentDTO commentDTO, Authentication authentication) {
        // Convertir el DTO a entidad
        Comment comment = CommentMapper.toEntity(commentDTO);

        // Verificar que se envíe el experimentId
        if (commentDTO.getExperimentId() == null) {
            throw new RuntimeException("Experiment ID is required to create a comment.");
        }

        // Recuperar el experimento a partir del experimentId
        Experiment experiment = experimentRepo.findById(commentDTO.getExperimentId())
                .orElseThrow(() -> new RuntimeException("Experiment not found with ID: " + commentDTO.getExperimentId()));
        comment.setExperiment(experiment);

        // Obtener la información del usuario autenticado a través de CustomeUserDetails
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // Buscar el UserEntity basado en el ID del usuario autenticado
        UserEntity user = userRepo.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userDetails.getId()));
        // Asignar el usuario autenticado como autor del comentario
        comment.setUser(user);

        // Guardar el comentario en la base de datos
        Comment savedComment = commentRepo.save(comment);

        return CommentMapper.toDTO(savedComment);
    }


    @Override
    public CommentDTO update(Long id, CommentDTO commentDTO) {
        Comment existingComment = commentRepo.findById(id).
                orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));
        if (existingComment == null) return null;
        existingComment.setContent(commentDTO.getContent());
        Comment savedComment = commentRepo.save(existingComment);
        return CommentMapper.toDTO(savedComment);
    }

    @Override
    public void delete(Long id) {
        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));
        commentRepo.delete(comment);
    }

    @Override
    public List<CommentDTO> findByUserId(Long userId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return commentRepo.findByUser(user).stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
