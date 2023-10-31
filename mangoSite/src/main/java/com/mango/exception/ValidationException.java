package com.mango.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends Exception{

	/**
	 *  Aggiunto il serialVersionUID 
	 *  Previene problemi di deserializzazione in eventuali modifiche future
	 */
	private static final long serialVersionUID = 1L;
	
	private String error;
	private String msg;
	
	public ValidationException() {
		super();
	}
	public ValidationException(String error, String msg) {
		super(msg);
		this.error = error;
		this.msg = msg;
	}
}
