package com.newtonbox.Controllers;

import com.newtonbox.Services.Impl.UserService;
import com.newtonbox.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    // Registro público de usuarios
    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO) {
        try{
            UserDTO saveUser = userService.save(userDTO);
            return ResponseEntity.ok(saveUser);
        } catch (RuntimeException e){
            log.error("Error registering user '{}' : {}", userDTO.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener info del usuario autenticado
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.notFound().build();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            UserDTO userDTO = userService.findByUsername(userDetails.getUsername());
            return ResponseEntity.ok(userDTO);
        }

        return ResponseEntity.status(403).build(); // Si no es UserDetails, denegamos
    }

}

