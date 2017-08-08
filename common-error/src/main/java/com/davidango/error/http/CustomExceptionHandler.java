package com.davidango.error.http;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.error.exception.UserCreationErrorException;

import lombok.Builder;
import lombok.Data;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(LookupErrorException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleLookupErrorException(LookupErrorException ex) {
		ErrorResponse error = ErrorResponse.builder().errorCode(1).errorMessage(ex.getMessage()).build();
		return error;
	}
	
	@ExceptionHandler(UserCreationErrorException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleUserCreationErrorException(UserCreationErrorException ex) {
		ErrorResponse error = ErrorResponse.builder().errorCode(2).errorMessage(ex.getMessage()).build();
		return error;
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String[] errorMessage = ex.getBindingResult().getFieldErrors().get(0).toString().split(";");
		List<String> errors = Arrays.stream(errorMessage).filter(s -> s.startsWith(" default message")).collect(Collectors.toList());
		ErrorResponse error = ErrorResponse.builder().errorCode(3).errorMessage(errors.get(0) + " : " + errors.get(1)).build();
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@Data
	@Builder
	public static class ErrorResponse {
		private Integer errorCode;
		private String errorMessage;
	}
}
