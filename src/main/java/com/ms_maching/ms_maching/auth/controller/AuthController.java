package com.ms_maching.ms_maching.auth.controller;


import com.ms_maching.ms_maching.auth.config.JwtUtil;
import com.ms_maching.ms_maching.auth.dto.AuthRequest;
import com.ms_maching.ms_maching.auth.model.Empresa;
import com.ms_maching.ms_maching.auth.model.Rol;
import com.ms_maching.ms_maching.auth.model.User;
import com.ms_maching.ms_maching.auth.repository.EmpresasRepositor;
import com.ms_maching.ms_maching.auth.repository.RolRepository;
import com.ms_maching.ms_maching.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder encoder;
    @Autowired private UserRepository userRepo;
    @Autowired private RolRepository rolRepo;
    @Autowired private EmpresasRepositor empRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest r) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(r.getEmail(), r.getPassword())
            );
            Optional<User> userOpt = userRepo.findByEmail(r.getEmail());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Long empresaId = user.getEmpresa() != null ? user.getEmpresa().getId() : null;
                String token = jwtUtil.generateToken(auth, empresaId);

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", user);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Usuario no encontrado"));
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest r) {
        if (userRepo.findByEmail(r.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Usuario ya existe");
        }
        if (r.getEmail()==null || r.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email obligatorio");
        }

        User u = new User();
        u.setUsername(r.getUsername());
        u.setPassword(encoder.encode(r.getPassword()));
        u.setEmail(r.getEmail());

        if (r.getEmpresaId()!=null) {
            Empresa emp = empRepo.findById(r.getEmpresaId())
                    .orElseThrow(() -> new IllegalArgumentException("Empresa no existe"));
            u.setEmpresa(emp);
        }

        // Asignar roles según la solicitud
        if (r.getRoles() != null && !r.getRoles().isEmpty()) {
            for (String roleName : r.getRoles()) {
                Rol role = rolRepo.findByName(roleName)
                        .orElseThrow(() -> new IllegalStateException("Rol " + roleName + " no existe"));
                u.getRoles().add(role);
            }
        } else {
            // Si no se especificaron roles, asignar USER por defecto
            Rol userRol = rolRepo.findByName("USER")
                    .orElseThrow(() -> new IllegalStateException("USER no existe"));
            u.getRoles().add(userRol);
        }

        try {
            userRepo.save(u);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado");
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error de unicidad");
        }
    }



    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(createErrorResponse("Token no proporcionado o formato incorrecto"));
        }

        String token = authHeader.substring(7);

        try {
            boolean isValid = jwtUtil.validateToken(token);

            if (isValid) {
                String username = jwtUtil.extractUsername(token);
                return ResponseEntity.ok(createSuccessResponse("Sesión activa", username));
            } else {
                return ResponseEntity.status(401).body(createErrorResponse("Sesión expirada"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(createErrorResponse("Error al validar la sesión: " + e.getMessage()));
        }
    }

    private Map<String, Object> createSuccessResponse(String message, String username) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("username", username);
        response.put("valid", true);
        return response;
    }

    private Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", errorMessage);
        response.put("valid", false);
        return response;
    }



    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> tokenRequest) {
        String token = tokenRequest.get("token");

        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body(createErrorResponse("Token no proporcionado"));
        }

        try {
            // Extraer el token si viene con el prefijo "Bearer "
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Validar el token
            boolean isValid = jwtUtil.validateToken(token);

            if (isValid) {
                String username = jwtUtil.extractUsername(token);
                return ResponseEntity.ok(createSuccessResponse("Token válido", username));
            } else {
                return ResponseEntity.status(401).body(createErrorResponse("Token inválido o expirado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(createErrorResponse("Error al validar el token: " + e.getMessage()));
        }
    }
}
