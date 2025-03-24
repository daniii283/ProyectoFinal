package com.newtonbox.Controllers;

import com.newtonbox.Security.CustomUserDetails;
import com.newtonbox.Services.Impl.CommentService;
import com.newtonbox.dto.CommentDTO;
import com.newtonbox.dto.ExperimentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/comments")
@PreAuthorize("isAuthenticated()")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // ------------------ Métodos CRUD ------------------

    // Obtener todos los comentarios
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CommentDTO>> findAll() {
        List<CommentDTO> commentsDTOList = commentService.findAll();
        return ResponseEntity.ok(commentsDTOList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @commentValidator.isCommentOwner(#id, authentication.principal.id) or @commentValidator.isParticipantInExperiment(#id, authentication.principal.id)")
    public ResponseEntity<CommentDTO> findById(@PathVariable Long id) {
        try {
            CommentDTO commentDTO = commentService.findById(id);
            return ResponseEntity.ok(commentDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    // Crear un comentario
    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @commentValidator.isParticipantOfExperiment(#commentDTO.experimentId, authentication.principal.id) or @commentValidator.isExperimentCreator(#commentDTO.experimentId, authentication.principal.id)")
    public ResponseEntity<CommentDTO> save(@RequestBody CommentDTO commentDTO, Authentication authentication) {
        if (commentDTO.getExperimentId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            CommentDTO savedComment = commentService.save(commentDTO, authentication);
            return ResponseEntity.ok(savedComment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    // Actualizar un comentario
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @commentValidator.isCommentOwner(#id, authentication.principal.id)")
    public ResponseEntity<CommentDTO> update(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.update(id, commentDTO);
            if (updatedComment == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedComment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Borrar un comentario
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @commentValidator.isCommentOwner(#id, authentication.principal.id) or @commentValidator.isCommentExperimentOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            commentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------ Métodos Personalizados ------------------

    // Buscar comentarios de un experimento
    @GetMapping("/experiment/{experimentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @experimentValidator.isCreator(#experimentId, authentication.principal.username) or @commentValidator.hasCommentInExperiment(#experimentId, authentication.principal.id)")
    public ResponseEntity<List<CommentDTO>> findbyExperiment(@PathVariable Long experimentId) {
        try {
            ExperimentDTO experimentDTO = new ExperimentDTO();
            experimentDTO.setId(experimentId);

            List<CommentDTO> commentDTOS = commentService.findByExperiment(experimentDTO);
            return commentDTOS.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(commentDTOS);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar comentarios de los experimentos que el usuario ha participado
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CommentDTO>> findMyComments(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<CommentDTO> myComments = commentService.findByUserId(userDetails.getId());
        return myComments.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(myComments);
    }


}
