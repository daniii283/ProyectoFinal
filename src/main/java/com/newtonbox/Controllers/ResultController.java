package com.newtonbox.Controllers;

import com.newtonbox.Models.Experiment;
import com.newtonbox.Models.Result;
import com.newtonbox.Services.Impl.ResultService;
import com.newtonbox.dto.ExperimentDTO;
import com.newtonbox.dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
@PreAuthorize("permitAll()")
public class ResultController {

    @Autowired
    private ResultService resultService;

    // ------------------ Métodos CRUD ------------------

    // Obtener todos los resultados de un proyecto
    @GetMapping
    public ResponseEntity<List<ResultDTO>> findAll() {
        List<ResultDTO> results = resultService.findAll();
        return ResponseEntity.ok(results);
    }

    // Obtener un resultado por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResultDTO> findById(@PathVariable Long id) {
        try {
            ResultDTO resultDTO = resultService.findById(id);
            return ResponseEntity.ok(resultDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un resultado
    @PostMapping("/save")
    public ResponseEntity<ResultDTO> save(@RequestBody ResultDTO resultDTO) {
        try{
            ResultDTO saved = resultService.save(resultDTO);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar un resultado
    @PutMapping("/{id}")
    public ResponseEntity<ResultDTO> update(@PathVariable Long id, @RequestBody ResultDTO resultDTO) {
        try {
            ResultDTO updated = resultService.update(id, resultDTO);
            if (updated == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Borrar un resultado
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            resultService.delete(id);
            System.out.println();
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------ Métodos Personalizado ------------------

    // Buscar resultados por un experimento en concreto
    @GetMapping("/byExperiment/{experimentId}")
    public ResponseEntity<List<ResultDTO>> findByExperiment(@PathVariable Long experimentId) {
        try {
            ExperimentDTO experimentDTO = new ExperimentDTO();
            experimentDTO.setId(experimentId);

            List<ResultDTO> results = resultService.findByExperiment(experimentDTO);

            if (results.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(results);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
