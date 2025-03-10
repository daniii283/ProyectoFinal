package com.newtonbox.Controllers;

import com.newtonbox.Models.UserEntity;
import com.newtonbox.Services.Impl.UserService;
import com.newtonbox.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO) {
        try{
            UserDTO saveUser = userService.save(userDTO);
            return ResponseEntity.ok(saveUser);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public String getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "No hay usuario autenticado";
        }
        return "Usuario autenticado: " + authentication.getName() + " - Roles: " + authentication.getAuthorities();
    }

}

