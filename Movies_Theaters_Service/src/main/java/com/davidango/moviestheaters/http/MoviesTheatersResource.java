package com.davidango.moviestheaters.http;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.moviestheaters.entities.Movie;
import com.davidango.moviestheaters.entities.Theater;
import com.davidango.moviestheaters.service.MovieService;
import com.davidango.moviestheaters.service.ShowTimesService;
import com.davidango.moviestheaters.service.TheaterService;

@RestController
@RequestMapping("internal/v1/api/")
public class MoviesTheatersResource {
	
	@Autowired
	private MovieService movieService;
	
	@Autowired 
	private TheaterService theaterService;
	
	@Autowired
	private ShowTimesService showTimesService;
	

	
	@RequestMapping(value = "movies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Movie>> searchMovies(@RequestParam(value = "name", required = false) String name,
													@RequestParam(value = "genre", required = false) String genre,
													@RequestParam(value = "rating", required = false) String rating) throws LookupErrorException {
						
		return new ResponseEntity<>(movieService.getListWithQueryParams(name, genre, rating), HttpStatus.OK);
	}
	
	@RequestMapping(value = "movie/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Movie> getMovie(@PathVariable(value = "id", required = true) String movieId) throws LookupErrorException {		
		return new ResponseEntity<>(movieService.getById(movieId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "theaters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Theater>> searchTheaters(@RequestParam(value = "name", required = false) String name,
														@RequestParam(value = "city", required = false) String city,
														@RequestParam(value = "state", required = false) String state,
														@RequestParam(value = "zipcode", required = false) String zipcode) throws LookupErrorException {
						
		return new ResponseEntity<>(theaterService.getListWithQueryParams(name, city, state, zipcode), HttpStatus.OK);
	}
	
	@RequestMapping(value = "theater/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Theater> getTheater(@PathVariable(value = "id", required = true) String theaterId) throws LookupErrorException {		
		return new ResponseEntity<>(theaterService.getById(theaterId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "showtimes/byTheater", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Theater>> getShowTimesForAllTheaters() throws LookupErrorException {		
		return new ResponseEntity<>(showTimesService.getAllTheaterShowTimes(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "showtimes/byTheater/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<Theater> getShowTimesByTheater(@PathVariable(value = "id", required = true) String theaterId) throws LookupErrorException {		
		return new ResponseEntity<>(showTimesService.getShowTimesByTheater(theaterId), HttpStatus.OK);
	}
	
	//movies currently playing today
	@RequestMapping(value = "showtimes/byMovie/today", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<List<Theater>> getTodaysShowTimes() {		
		return new ResponseEntity<>(showTimesService.getTodaysShowTimes(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "showtimes/byMovie/today/location", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<List<Theater>> getTodaysShowTimesWithLocation(
					@RequestParam(value = "city", required = false) String city,
					@RequestParam(value = "zipcode", required = false) String zipcode) throws LookupErrorException {		
		return new ResponseEntity<>(showTimesService.getTodaysShowTimesWithLocationParams(city, zipcode), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String home() {
		return "Spring Boot is up!";
	}
}
