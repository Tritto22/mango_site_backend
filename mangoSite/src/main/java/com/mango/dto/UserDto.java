package com.mango.dto;

import java.io.Serializable; // impacchetta dato come fosse Json per l'invio in rete o memorizzazione su disco rigido

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto implements Serializable{
	private static final long serialVersionUID = 1L; // Aggiunto il serialVersionUID che previene problemi di deserializzazione in modifiche future
	
	private String username;
	
	private String email;
	
	private String password;
	
}
