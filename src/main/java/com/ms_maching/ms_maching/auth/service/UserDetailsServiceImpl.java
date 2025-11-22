package com.ms_maching.ms_maching.auth.service;


import com.ms_maching.ms_maching.auth.model.Rol;
import com.ms_maching.ms_maching.auth.model.User;
import com.ms_maching.ms_maching.auth.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repo;
    public UserDetailsServiceImpl(UserRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User appUser = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        var authorities = appUser.getRoles().stream()
                .map(Rol::getName)                 // "ADMIN" o "USER"
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername()                                                                                                                             ,
                appUser.getPassword(),
                authorities
        );
    }
}
