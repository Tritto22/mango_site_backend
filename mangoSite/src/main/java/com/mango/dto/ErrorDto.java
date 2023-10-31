package com.mango.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String error;
	private String msg;
}
