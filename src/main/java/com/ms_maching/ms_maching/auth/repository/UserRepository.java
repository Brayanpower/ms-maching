package com.ms_maching.ms_maching.auth.repository;


import com.ms_maching.ms_maching.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

}
