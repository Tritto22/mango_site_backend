package com.mango.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mango.dto.ErrorDto;
import com.mango.dto.ResponseDto;
import com.mango.dto.UserDto;
import com.mango.entity.User;
import com.mango.exception.ValidationException;
import com.mango.repository.UserRepository;
import com.mango.security.services.UserDetailsImpl;
import com.mango.security.services.UserLoggedIn;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	PasswordEncoder encoder;
	
	// PUT
	public ResponseDto updateUser(UserDto user) {
		
		User currentUser = null;
		UserDetailsImpl userLogged = null;
		UserLoggedIn userLoggedIn = new UserLoggedIn();
		ResponseDto response = new ResponseDto();
		
		try {
		
			userValidation(user, currentUser);
			userLogged = userLoggedIn.takeUserLogged();
			currentUser = repository.findByUsername(userLogged.getUsername()).orElse(null);
			currentUser.setEmail(user.getEmail());
			currentUser.setUsername(user.getUsername());
			if(user.getPassword()!= null && StringUtils.hasText(user.getPassword())) {
				currentUser.setPassword(encoder.encode(user.getPassword()));
			}
			currentUser = repository.save(currentUser);
			
			UserDto updatedUser = new UserDto();

			updatedUser.setEmail(currentUser.getEmail());
			updatedUser.setUsername(currentUser.getUsername());
			
			response.setPayload(updatedUser);
			
		} catch(Exception e) {
			ErrorDto error = new ErrorDto();
			error.setMsg(e.getMessage());
			response.setError(error);
		}
		return response;
	}
	
	private void userValidation(UserDto user, User currentUser) throws ValidationException {
		if(user != null) {
			if(StringUtils.hasText(user.getEmail())) {
				String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

				if(!user.getEmail().matches(emailRegex)) {
					throw new ValidationException("01", "email non valida!");
				}
			} else {
				throw new ValidationException("01", "Il campo email non può essere vuoto!");
			}
			
			if(StringUtils.hasText(user.getUsername())) {
				if(user.getUsername().length() < 3 || user.getUsername().length() > 20) {
					throw new ValidationException("02", "username non valida!");
				}
			} else {
				throw new ValidationException("02", "Il campo username non può essere vuoto!");
			}
					
//			if(user.getId() != null) {
//				currentUser = repository.findById(user.getId()).orElse(null);
//				
//				if(currentUser == null) {
//					throw new ValidationException("03", "L'utente che vuoi modificare non esiste!");
//				}
//			} else {
//				throw new ValidationException("03", "L'id utente non può essere null!");
//			}
		}
	}
}
