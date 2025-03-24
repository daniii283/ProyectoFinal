package com.newtonbox.Controllers;

import com.newtonbox.Models.UserEntity;
import com.newtonbox.Services.Impl.UserService;
import com.newtonbox.dto.ChangePasswordDTO;
import com.newtonbox.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")

public class UserController {

    @Autowired
    private UserService userService;

    // ------------------ Métodos CRUD ------------------

    // Obtener todos los usuarios (Solo ADMIN)
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // Obtener un usuario por ID
    // -> Solo ADMIN o si es el usuario auntenticado
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        try {
            UserDTO userDTO = userService.findById(id);
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /* La petición POST sera manejado en otro controlador */

    @PutMapping("/{id}/changePassword")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO changePasswordDTO){
        try {
            userService.changePassword(id, changePasswordDTO);
            return ResponseEntity.ok("Password updated succesfully");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Borrar un usuario (Solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();

        }
    }

    // ------------------ Métodos Personalizados ------------------

    // Buscar un usuario por su nombre
    // -> Solo ADmin o si es el mismo usuario autenticado
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username ")
    public ResponseEntity<UserDTO> findByUsername(@PathVariable String username) {
        try {
            UserDTO user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Verificar si existe un usuario con cierto nombre (Solo ADMIN)
    @GetMapping("/exist/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> existByUsername(@PathVariable String username) {
        try {
            boolean exist = userService.existByUsername(username);
            return ResponseEntity.ok(exist);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
