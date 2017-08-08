package com.davidango.moviestheaters.service.impl;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.List;

import org.springframework.stereotype.Service;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.moviestheaters.entities.Theater;
import com.davidango.moviestheaters.repository.TheaterRepository;
import com.davidango.moviestheaters.service.TheaterService;

@Service
public class TheaterServiceImpl extends BaseListServiceImpl<Theater, TheaterRepository> implements TheaterService {


	public TheaterServiceImpl() {
		super("Theater");
	}
	
	@Override
	public List<Theater> getListWithQueryParams(String... params) throws LookupErrorException {
		List<Theater> baseListForSession = getAllAvailableTheaters();

		if (!isEmpty(params[0]))   //name
			baseListForSession = processListWithName(params[0], baseListForSession);

		if (!isEmpty(params[1]))  //city
			baseListForSession = processList(baseListForSession, "getCity", params[1]);
		
		if (!isEmpty(params[2]))  //state
			baseListForSession = processListWithEnum("State", Theater.States.getStringNames(), params[2], "getState", baseListForSession);
		
		if (!isEmpty(params[3]))  //zipcode
			baseListForSession = processList(baseListForSession, "getZipcode", params[3]);
		
		return baseListForSession;
	}
	
	@Override
	public List<Theater> modifyExistingListWithQueryParams(List<Theater> baseList, String city, String zipcode) {
		if (!isEmpty(city))  //city
			baseList = processList(baseList, "getCity", city);
		
		if (!isEmpty(zipcode))  //zipcode
			baseList = processList(baseList, "getZipcode", zipcode);
		
		return baseList;
	}
	
	private List<Theater> getAllAvailableTheaters() {
		List<Theater> list = getAllAvailable();
		list.stream().forEach(t -> t.setMovieShowTimes(null));
		return list;
	}
	
	@Override
	public Theater getById(String id) throws LookupErrorException {
		Theater theater = super.getById(id);
		theater.setMovieShowTimes(null);
		return theater;
	}
}
