package com.newtonbox.Services.Impl;

import com.newtonbox.Models.Role;
import com.newtonbox.Models.UserEntity;
import com.newtonbox.Repository.IRoleRepository;
import com.newtonbox.Repository.IUserRepository;
import com.newtonbox.Services.IUserService;
import com.newtonbox.dto.UserDTO;
import com.newtonbox.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;


@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private IRoleRepository roleRepo;

    @Override
    public List<UserDTO> findAll() {
        return userRepo.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
       UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID:" + id));

       return UserMapper.toDTO(user);
    }

    // --- Personalizado--
    @Override
    public UserDTO findByUsername(String username) {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User " + username + " not found"));

        return UserMapper.toDTO(user);

    }

    // --- Personalizado--
    @Override
    public boolean existByUsername(String username) {
        return userRepo.existsByUsername(username);
    }



    @Override
    public UserDTO save(UserDTO userDTO) {
        UserEntity user  = UserMapper.toEntity(userDTO);
        UserEntity userSaved = userRepo.save(user);
        return UserMapper.toDTO(userSaved);
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        // 1. Buscar en la base de datos
        UserEntity existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // 2. Si la contraseña no es nula ni vacia, actualizaria (sin encriptar)
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(userDTO.getPassword()); // ¡Aquí no se encripta!
        }

        // 3. Si los roles no son nulos ni vacios, asignarlos correctamente
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()){
            Set<Role> updateRoles = userDTO.getRoles().stream()
                    .map(roleDTO -> roleRepo.findById(roleDTO.getId())
                            .orElseThrow( () -> new RuntimeException("Role not found with ID: " + roleDTO.getId())))
                            .collect(Collectors.toSet());
            existingUser.setRoles(updateRoles);
        }

        // 4. Guaradr cambios en la base de datos
        UserEntity savedUser = userRepo.save(existingUser);

        // 5. Convertir la entidad a DTO y devolverla
        return UserMapper.toDTO(savedUser);
    }

    @Override
    public void delete(Long id) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        userRepo.delete(user);
    }



}
