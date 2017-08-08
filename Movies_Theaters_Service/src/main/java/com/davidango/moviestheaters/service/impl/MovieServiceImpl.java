package com.davidango.moviestheaters.service.impl;

import static org.springframework.util.StringUtils.isEmpty;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.moviestheaters.entities.Movie;
import com.davidango.moviestheaters.repository.MovieRepository;
import com.davidango.moviestheaters.service.MovieService;

@Service
public class MovieServiceImpl extends BaseListServiceImpl<Movie, MovieRepository> implements MovieService {
	
	public MovieServiceImpl() {
		super("Movie");
	}
	
	@Override
	public List<Movie> getListWithQueryParams(String... params) throws LookupErrorException {
		
		List<Movie> baseListForSession = getAllAvailableMovies();

		if (!isEmpty(params[0]))   //name
			baseListForSession = processListWithName(params[0], baseListForSession);

		if (!isEmpty(params[1]))  //genre
			baseListForSession = processListWithEnum("genre", Movie.Genre.getStringNames(), params[1], "getGenre", baseListForSession);
			
		if (!isEmpty(params[2]))  //rating
			baseListForSession = processListWithEnum("rating", Movie.Rating.getStringNames(), params[2], "getRating", baseListForSession);
		
		return baseListForSession;	
	}
	
	@Override
	public Movie getById(String id) throws LookupErrorException {
		return translateOpeningDateFromDatabase(super.getById(id));
	}

	private List<Movie> getAllAvailableMovies() {
		List<Movie> movies = super.getAllAvailable();
		movies.forEach(m -> translateOpeningDateFromDatabase(m));
		return movies;
	}

	public static String getIsoDate(Integer daysDelta) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.systemDefault())
				.format(Instant.now().plus(Duration.ofDays(daysDelta)));
	}
	
	public static Movie translateOpeningDateFromDatabase(Movie movie) {
		try {
			Integer.parseInt(movie.getOpeningDate());
		} catch (NumberFormatException e) {
			return movie;   //already cached and parsed
		}
		movie.setOpeningDate(getIsoDate(Integer.parseInt(movie.getOpeningDate())));
		return movie;
	}
}
