package com.mango.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mango.models.User;
import com.mango.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	// PUT
	public User updateUser(Long id, User user) {
		User currentUser = repository.findById(id).orElse(null);
		
		if(user.getEmail()!= null) {
			currentUser.setEmail(user.getEmail());
		}
		if(user.getUsername()!= null) {
			currentUser.setUsername(user.getUsername());
		}
		if(user.getPassword()!= null) {
			currentUser.setPassword(user.getPassword());
		}
		return repository.save(currentUser);
	}
}
