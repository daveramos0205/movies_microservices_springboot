package com.davidango.useraccounts.http;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.error.exception.UserCreationErrorException;
import com.davidango.useraccounts.externalresources.Theater;
import com.davidango.useraccounts.http.entities.HttpUser;
import com.davidango.useraccounts.service.UsersService;


@RestController
@RequestMapping("internal/v1/api/")
public class UsersAccountsResource {
	
	@Autowired
	private UsersService usersService;
	
	
	@RequestMapping(method = RequestMethod.POST, value = "createUser", 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpUser> createAccount(@Valid @RequestBody HttpUser newAccount) throws UserCreationErrorException {
		return new ResponseEntity<>(usersService.createAccount(newAccount), HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getByUserName/{userName}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpUser> getAccountByUserName(@PathVariable(value="userName", required=true) String userName) throws LookupErrorException {
		return new ResponseEntity<>(usersService.getByUserName(userName), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getTodaysMoviesByUserCity/{userName}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Theater>> getTodaysMoviesByUserCity(@PathVariable(value="userName", required=true) String userName) throws LookupErrorException {
		return new ResponseEntity<>(usersService.getTodaysMoviesByUserCity(userName), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getTodaysMoviesByUserZipcode/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Theater>> getTodaysMoviesByUserZipcode(@PathVariable(value="userName", required=true) String userName) throws LookupErrorException {
		return new ResponseEntity<>(usersService.getTodaysMoviesByUserZipcode(userName), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String home() {
		return "Spring Boot is up!";
	}
}
