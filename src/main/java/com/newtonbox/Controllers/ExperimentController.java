package com.newtonbox.Controllers;

import com.newtonbox.Security.CustomUserDetails;
import com.newtonbox.Services.Impl.ExperimentService;
import com.newtonbox.dto.ExperimentDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experiments")
@PreAuthorize("isAuthenticated()")
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;

    // ------------------Métodos CRUD------------------

    // Obtener todos los experimentos (Solo ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ExperimentDTO>> findAll() {
        List<ExperimentDTO> experiments = experimentService.findAll();
        return ResponseEntity.ok(experiments);
    }

    // Obtener un experimento por ID
    // -> ADMIN o si el usuario es creador/participante.

    @GetMapping("/search/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @experimentValidator.isUserAllowed(#id, authentication.principal.username)")
    public ResponseEntity<ExperimentDTO> findById(@PathVariable Long id) {
        try {
            ExperimentDTO experimentDTO = experimentService.findById(id);
            return ResponseEntity.ok(experimentDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExperimentDTO> save(@RequestBody ExperimentDTO experimentDTO, Authentication authentication) {
        try {
            ExperimentDTO savedExp = experimentService.save(experimentDTO, authentication);
            return ResponseEntity.ok(savedExp);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar un experimento
    // -> Solo ADMIN o si el usuario es el creador (usamos isCreator)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @experimentValidator.isCreator(#id, authentication.principal.username)")
    public ResponseEntity<ExperimentDTO> update(@PathVariable Long id, @RequestBody ExperimentDTO experimentDTO) {
        try {
            ExperimentDTO updateExp = experimentService.update(id, experimentDTO);
            if(updateExp == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updateExp);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Borrar un experimento (Solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @experimentValidator.isCreator(#id, authentication.principal.username)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            experimentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    // ------------------Métodos Personalizados------------------

    // Buscar experimentos por el usuario que los creó (Solo ADMIN o si es el mismo usuario
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<ExperimentDTO>> findByCreatedBy(@PathVariable Long userId) {
        try {
            List<ExperimentDTO> experiments = experimentService.findByCreatedBy(userId);
            if (experiments.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(experiments);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ExperimentDTO>> findMyExperiments(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        List<ExperimentDTO> experiments = experimentService.findByCreatedByUsername(username);
        return experiments.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(experiments);
    }

}
