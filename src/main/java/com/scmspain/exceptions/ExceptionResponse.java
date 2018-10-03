package com.scmspain.exceptions;

import java.util.List;

public class ExceptionResponse {
	
	public String message;
	public List<ResponseError> errors;
	
	public ExceptionResponse(String message) {
		this.message = message;
	}
	
	public ExceptionResponse(String message, List<ResponseError> errors) {
		this.message = message;
		this.errors = errors;
	}
	
}