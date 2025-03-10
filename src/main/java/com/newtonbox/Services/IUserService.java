package com.newtonbox.Services;

import com.newtonbox.Models.UserEntity;
import com.newtonbox.dto.UserDTO;

import java.util.List;

public interface IUserService {

    List<UserDTO> findAll();                   // Obtener todos
    UserDTO findById(Long id);                // Obtener por ID
    UserDTO findByUsername(String username); // Nombre personalizado
    boolean existByUsername(String username);
    UserDTO save(UserDTO user); // Crear o guardar
    UserDTO update(Long id, UserDTO user);       // Actualizar
    void delete(Long id);                  // Eliminar
}
