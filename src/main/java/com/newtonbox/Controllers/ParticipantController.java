package com.newtonbox.Controllers;

import com.newtonbox.Models.Experiment;
import com.newtonbox.Models.Participant;
import com.newtonbox.Models.UserEntity;
import com.newtonbox.Services.Impl.ParticipantService;;
import com.newtonbox.dto.ParticipantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participants")
@PreAuthorize("permitAll()")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    // ------------------ Métodos CRUD ------------------

    // Obtener todos los participantes de un proyecto
    @GetMapping
    public ResponseEntity<List<ParticipantDTO>> findAll() {
        List<ParticipantDTO> participants = participantService.findAll();
        return ResponseEntity.ok(participants);
    }

    // Obtener un participante por ID
    @GetMapping("/{id}")
    public ResponseEntity<ParticipantDTO> findById(@PathVariable Long id) {
        try {
            ParticipantDTO participantDTO = participantService.findById(id);
            return ResponseEntity.ok(participantDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un participante
    @PostMapping("/save")
    public ResponseEntity<ParticipantDTO> save(@RequestBody ParticipantDTO participantDTO) {
        try{
            ParticipantDTO saved = participantService.save(participantDTO);
            return ResponseEntity.ok(saved);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar un participante
    @PutMapping("/{id}")
    public ResponseEntity<ParticipantDTO> update(@PathVariable Long id, @RequestBody ParticipantDTO participantDTO) {
        try {
            ParticipantDTO updated = participantService.update(id, participantDTO);
            if (updated == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Borrar un participante de un proyecto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            participantService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------ Métodos Personalizados ------------------

    // Buscar participantes de un experimento
    @GetMapping("/{expId}")
    public ResponseEntity<List<ParticipantDTO>> findByExperiment(@PathVariable Long expId) {
        try {
            List<ParticipantDTO> participants = participantService.findByExperiment(expId);
            if (participants == null || participants.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(participants);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar participantes por usuario(qué experimentos y roles asume un usuario
    @GetMapping("/{userId}")
    public ResponseEntity<List<ParticipantDTO>> findByUser(@PathVariable Long userId) {
        try {
            List<ParticipantDTO> participants = participantService.findByUser(userId);
            if (participants.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(participants);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}