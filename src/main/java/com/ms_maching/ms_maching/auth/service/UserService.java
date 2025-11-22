package com.ms_maching.ms_maching.auth.service;


import com.ms_maching.ms_maching.auth.model.User;
import com.ms_maching.ms_maching.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String password) {
        // Verificar si el usuario ya existe
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        // Codificar la contraseña
        String encodedPassword = passwordEncoder.encode(password);

        // Guardar el usuario en la base de datos
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }
}