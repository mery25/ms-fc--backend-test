package com.scmspain.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.scmspain.controller.TweetController;
import com.scmspain.exceptions.ExceptionResponse;
import com.scmspain.exceptions.ResponseError;

@EnableWebMvc
@ControllerAdvice(assignableTypes = TweetController.class)
public class CustomExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public final ResponseEntity<ExceptionResponse> invalidValidationException(ValidationException ex) {
		if (ex instanceof ConstraintViolationException) {
		    List<ResponseError> errors = ((ConstraintViolationException) ex).getConstraintViolations().stream()
		    		                                                                                  .map(c -> new ResponseError("" + c.getPropertyPath(), c.getMessage()))
		    		                                                                                  .collect(Collectors.toList());

		    
			return new ResponseEntity<>(new ExceptionResponse("Invalid field(s)", errors), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new ExceptionResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(TweetNotFoundException.class)
	@ResponseBody
	public final ResponseEntity<ExceptionResponse> invalidTweetNotFoundException(TweetNotFoundException ex) {
		return new ResponseEntity<>(new ExceptionResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
	}
	
}