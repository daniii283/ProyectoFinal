package com.newtonbox.Controllers;

import com.newtonbox.Models.Comment;
import com.newtonbox.Models.Experiment;
import com.newtonbox.Services.Impl.CommentService;
import com.newtonbox.dto.CommentDTO;
import com.newtonbox.dto.ExperimentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/comments")
@PreAuthorize("permitAll()")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // ------------------ Métodos CRUD ------------------

    // Obtener todos los comentarios
    @GetMapping
    public ResponseEntity<List<CommentDTO>> findAll() {
        List<CommentDTO> commentsDTOList = commentService.findAll();
        return ResponseEntity.ok(commentsDTOList);
    }

    // Obtener un comentario por ID
    @GetMapping("/{id}")
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
    public ResponseEntity<CommentDTO> save(@RequestBody CommentDTO commentDTO) {
        try{
            CommentDTO savedComment = commentService.save(commentDTO);
            return ResponseEntity.ok(savedComment);
        }catch(RuntimeException e){
         return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar un comentario
    @PutMapping("{id}")
    public ResponseEntity<CommentDTO> update(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.update(id, commentDTO);
            if(updatedComment == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedComment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Borrar un comentario
    @DeleteMapping("/{id}")
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
    public ResponseEntity<List<CommentDTO>> findbyExperiment(@PathVariable Long experimentId) {
        try {
            ExperimentDTO experimentDTO = new ExperimentDTO();
            experimentDTO.setId(experimentId);

            List<CommentDTO> commentDTOS = commentService.findByExperiment(experimentDTO);
            if (commentDTOS.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(commentDTOS);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
