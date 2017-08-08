package com.davidango.moviestheaters.service.impl;

import static com.davidango.moviestheaters.service.impl.MovieServiceImpl.getIsoDate;
import static com.davidango.moviestheaters.service.impl.MovieServiceImpl.translateOpeningDateFromDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.moviestheaters.entities.MovieShowTimes;
import com.davidango.moviestheaters.entities.Theater;
import com.davidango.moviestheaters.repository.TheaterRepository;
import com.davidango.moviestheaters.service.ShowTimesService;
import com.davidango.moviestheaters.service.TheaterService;

@Service
public class ShowTimesServiceImpl implements ShowTimesService {
	
	@Autowired
	private TheaterRepository theaterRepository;
	
	@Autowired
	private TheaterService theaterService;

	
	@Override
	public Theater getShowTimesByTheater(String id) throws LookupErrorException {
		
		Integer theaterId;   //make common method
		try {
			theaterId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			throw new LookupErrorException("theater id must be an integer");
		}
		Theater theater = theaterRepository.findOne(theaterId);
		
		if (theater == null)
			throw new LookupErrorException("could not find theater id " + id);
		
		return transformDates(theater);
	}
	
	@Override
	public List<Theater> getAllTheaterShowTimes() {
		List<Theater> theaters = (List<Theater>)theaterRepository.findAll();
		theaters.stream().forEach(i -> transformDates(i));
		return theaters;
	}
	
	@Override
	public List<Theater> getTodaysShowTimes() {

		List<Theater> theaters = (List<Theater>) theaterRepository.findAll();
		
		theaters.stream().forEach(i -> transformDates(i));
		theaters.stream().forEach(i -> filterMovieShowTimesByCurrentlyPlaying(i));
		theaters = theaters.stream().filter(i -> i.getMovieShowTimes() != null).collect(Collectors.toList());
		theaters.stream().forEach(i -> clearAllDates(i.getMovieShowTimes()));
		
		return sortByTheaterName(theaters);
	}
	
	
	@Override
	public List<Theater> getTodaysShowTimesWithLocationParams(String city, String zipcode) {
		List<Theater> results  = getTodaysShowTimes();
		return sortByTheaterName(theaterService.modifyExistingListWithQueryParams(results, city, zipcode));
	}
	
	private void clearAllDates(List<MovieShowTimes> mst) {
		mst.stream().forEach(i -> {
			i.setClosingDate(null);
			i.getMovie().setOpeningDate(null);
		});
	}
	
	private Theater filterMovieShowTimesByCurrentlyPlaying(Theater theater) {
		List<MovieShowTimes> mstList = theater.getMovieShowTimes();		
		List<MovieShowTimes> currentlyPlayingMst = mstList.stream().filter(mst 
				-> checkIfMovieIsCurrentlyPlaying(mst)).collect(Collectors.toList());
		
		if (!currentlyPlayingMst.isEmpty())
			theater.setMovieShowTimes(currentlyPlayingMst);
		else
			theater.setMovieShowTimes(null);
		return theater;
	}
	
	private Theater transformDates(Theater theater) {
		
		List<MovieShowTimes> mst = theater.getMovieShowTimes();
		mst.stream().forEach(i -> {
			translateOpeningDateFromDatabase(i.getMovie());
			try {
				i.setClosingDate(getIsoDate(Integer.parseInt(i.getClosingDate())));
			} catch (NumberFormatException e) {}  //already formatted, squelch
		});
		return theater;
	}
	
	private List<Theater> sortByTheaterName(List<Theater> theaters) {
		Collections.sort(theaters, new Comparator<Theater>() {
			@Override
			public int compare(Theater l1, Theater l2) {
				return l1.getName().compareTo(l2.getName());
			}
		});
		return theaters;
	}

	
	private Boolean checkIfMovieIsCurrentlyPlaying(MovieShowTimes mst) {
		try {
			Date openDate = new SimpleDateFormat("yyyy-MM-dd")
					.parse(mst.getMovie().getOpeningDate());
			
			Date closeDate = new SimpleDateFormat("yyyy-MM-dd")
					.parse(mst.getClosingDate());
			
			Date todaysDate = new Date();
			
			if (todaysDate.compareTo(openDate) >= 0 &&
					todaysDate.compareTo(closeDate) <= 0)
				return true;
			
			return false;
		
		} catch (ParseException e) {   //unreachable as it's internal, squelcheable 
			e.printStackTrace();
		}	
		return false;
	}
}
