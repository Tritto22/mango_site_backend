package com.mango.security.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mango.exception.ValidationException;

public class UserLoggedIn {
	
	public UserDetailsImpl takeUserLogged() throws ValidationException{
        // Ottieni l'oggetto Authentication dal SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userLoggedDetails = null;

    	// Verifica se l'utente è autenticato
        if (authentication != null && authentication.isAuthenticated()) {
            // Ottieni le informazioni sull'utente corrente
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl) {
            	userLoggedDetails = (UserDetailsImpl) principal;
            	
            }
        }else {
        	throw new ValidationException("04", "Nessun utente Loggato, effettua nuovamente il login per procedere.");
        }
        return userLoggedDetails;
        
    }
}
