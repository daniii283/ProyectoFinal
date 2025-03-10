package com.newtonbox.Services;

import com.newtonbox.Models.Comment;
import com.newtonbox.dto.CommentDTO;
import com.newtonbox.dto.ExperimentDTO;

import java.util.List;

public interface ICommentService {

    List<CommentDTO> findAll();
    CommentDTO findById(Long id);
    List<CommentDTO> findByExperiment(ExperimentDTO experimentDTO);  // Personalizado
    CommentDTO save(CommentDTO commentDTO);
    CommentDTO update(Long id, CommentDTO commentDTO);
    void delete(Long id);
}
