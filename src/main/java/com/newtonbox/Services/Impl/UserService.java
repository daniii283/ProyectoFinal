package com.newtonbox.Services.Impl;

import com.newtonbox.Models.Role;
import com.newtonbox.Models.UserEntity;
import com.newtonbox.Repository.IRoleRepository;
import com.newtonbox.Repository.IUserRepository;
import com.newtonbox.Services.IUserService;
import com.newtonbox.dto.ChangePasswordDTO;
import com.newtonbox.dto.UserDTO;
import com.newtonbox.mapper.UserMapper;
import com.newtonbox.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.IllegalFormatCodePointException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;


@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    private IRoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        UserEntity user = UserMapper.toEntity(userDTO);

        if (user.getPassword() != null && !user.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }else {
            throw new RuntimeException("Password is required for registration");
        }

        // Asignar rol por defecto: RESEARCHER
        Role defaultRole = roleRepo.findByRoleEnum(RoleEnum.RESEARCHER)
                .orElseThrow(() -> new RuntimeException("Default role RESEARCHER not found"));
        user.getRoles().add(defaultRole);

        // Guardar en bases de datos
        UserEntity userSaved = userRepo.save(user);

        // Devolver el DTO
        return UserMapper.toDTO(userSaved);
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        // 1. Buscar en la base de datos
        UserEntity existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // 2. Si el nombre no es nulo ni vacio, actualizarlo
        if (userDTO.getUsername() != null) {
            existingUser.setUsername(userDTO.getUsername());
        }
        // 3. Si los roles no son nulos ni vacíos, asignarlos correctamente
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            Set<Role> updateRoles = userDTO.getRoles().stream()
                    .map(roleDTO -> roleRepo.findById(roleDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleDTO.getId())))
                    .collect(Collectors.toSet());
            existingUser.setRoles(updateRoles);
        }

        // 4. Guardar los cambios en la base de datos
        UserEntity savedUser = userRepo.save(existingUser);

        // 5. Convertir la entidad a DTO y devolverla
        return UserMapper.toDTO(savedUser);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        // 1. Buscar el usuario en la base de datos
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // 2. Verificar la contraseña actual con la que esta guardada en BD
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())){
            throw new RuntimeException("The current password is not correct");
        }

        // 3. Comprobar que newPassword y confirmNewPassword coincidan
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())){
            throw new RuntimeException("The new passwords do not match");
        }
        // 4. Encriptar la nueva contraseña y guardarla
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepo.save(user);
    }


    @Override
    public void delete(Long id) {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        userRepo.delete(user);
    }
}
