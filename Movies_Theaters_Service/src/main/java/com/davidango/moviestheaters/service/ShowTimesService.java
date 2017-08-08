package com.davidango.moviestheaters.service;

import java.util.List;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.moviestheaters.entities.Theater;

public interface ShowTimesService {

	Theater getShowTimesByTheater(String id) throws LookupErrorException;
	
	List<Theater> getAllTheaterShowTimes();
	
	List<Theater> getTodaysShowTimes();

	List<Theater> getTodaysShowTimesWithLocationParams(String city, String zipcode);
}
