package com.newtonbox.Controllers;

import com.newtonbox.Models.Experiment;
import com.newtonbox.Models.UserEntity;
import com.newtonbox.Services.Impl.ExperimentService;
import com.newtonbox.dto.ExperimentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experiments")
@PreAuthorize("isAuthenticated()")
public class ExperimentController {

    @Autowired
    @Qualifier("experimentService")
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
    // -> ADMIN o si el usuario es creador/participante. Para esto utilizamos el metodo isUserAllowed del servicio

    @GetMapping("/search/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @experimentService.isUserAllowed(#id, authentication.principal.username)")
    public ResponseEntity<ExperimentDTO> findById(@PathVariable Long id) {
        try {
            ExperimentDTO experimentDTO = experimentService.findById(id);
            return ResponseEntity.ok(experimentDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un experimento (Solo ADMIN o RESEARCHER)
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RESEARCHER')")
    public ResponseEntity<ExperimentDTO> save(@RequestBody ExperimentDTO experimentDTO) {
        try{
            ExperimentDTO savedExp = experimentService.save(experimentDTO);
            return ResponseEntity.ok(savedExp);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar un experimento
    // -> Solo ADMIN o si el usuario es el creador (usamos isCreator)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @experimentService.isCreator(#id, authentication.principal.username)")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            experimentService.delete(id);
            return ResponseEntity.notFound().build();
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
            UserEntity userCreator = new UserEntity();
            userCreator.setId(userId);

            List<ExperimentDTO> experiments = experimentService.findByCreatedBy(userCreator);
            if (experiments.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(experiments);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
