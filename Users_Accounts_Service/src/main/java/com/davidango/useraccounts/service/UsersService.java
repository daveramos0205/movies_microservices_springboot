package com.davidango.useraccounts.service;

import java.util.List;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.error.exception.UserCreationErrorException;
import com.davidango.useraccounts.externalresources.Theater;
import com.davidango.useraccounts.http.entities.HttpUser;

public interface UsersService {

	HttpUser createAccount(HttpUser newAccount) throws UserCreationErrorException;

	HttpUser getByUserName(String userName) throws LookupErrorException;
	
	List<Theater> getTodaysMoviesByUserCity(String userName) throws LookupErrorException;
	
	List<Theater> getTodaysMoviesByUserZipcode(String userName) throws LookupErrorException;
	
}
