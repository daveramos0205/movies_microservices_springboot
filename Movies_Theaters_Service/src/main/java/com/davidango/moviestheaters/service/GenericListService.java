package com.davidango.moviestheaters.service;

import java.util.List;

import com.davidango.error.exception.LookupErrorException;

public interface GenericListService<T> {
	
	List<T> getListWithQueryParams(String... params) throws LookupErrorException;
	
	T getById(String id) throws LookupErrorException;
}

