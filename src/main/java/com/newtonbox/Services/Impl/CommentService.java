package com.newtonbox.Services.Impl;

import com.newtonbox.Models.Comment;
import com.newtonbox.Models.Experiment;
import com.newtonbox.Repository.ICommentRepository;
import com.newtonbox.Services.ICommentService;
import com.newtonbox.dto.CommentDTO;
import com.newtonbox.dto.ExperimentDTO;
import com.newtonbox.mapper.CommentMapper;
import com.newtonbox.mapper.ExperimentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private ICommentRepository commentRepo;

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
    public CommentDTO save(CommentDTO commentDTO) {
        Comment comment = CommentMapper.toEntity(commentDTO);
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
}
