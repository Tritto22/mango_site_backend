package com.mango.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mango.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findById(Long id);
	Optional<User> findByUsername(String username);
	Optional<User> findByUsernameAndEmail(String username, String email);

	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
}
