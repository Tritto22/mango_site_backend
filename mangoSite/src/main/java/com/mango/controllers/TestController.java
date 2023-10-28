package com.mango.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mango.models.User;
import com.mango.payload.response.MessageResponse;
import com.mango.security.services.UserDetailsImpl;
import com.mango.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

	@Autowired
	private UserService service;
	
	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
	    return "User Content.";
	}
	
	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
	    return "Moderator Board.";
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
	    return "Admin Board.";
	}
	
	@PutMapping("/user/update")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) {
		Long userId = ((UserDetailsImpl) userDetails).getId();
		
//		// Controllo che l'utente che sta cercando di aggiornare il profilo sia lo stesso dell'utente autenticato
//        if (!userId.equals(user.getId())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non sei autorizzato a eseguire questa azione.");
//        }
		if(user.getPassword()!= null && !user.getPassword().isEmpty()) {
			user.setPassword(encoder.encode(user.getPassword()));
		}
        // Aggiornamento del profilo utente
        User updatedUser = service.updateUser(userId, user);
        
        if (updatedUser != null) {
            return ResponseEntity.ok(new MessageResponse("Modifica avvenuta con successo ora effettua nuovamente il Login!"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'aggiornamento del profilo utente.");
        }
	}
}
