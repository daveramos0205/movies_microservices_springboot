package com.davidango.moviestheaters.service;

import java.util.List;

import com.davidango.moviestheaters.entities.Theater;

public interface TheaterService extends GenericListService<Theater>{

	List<Theater> modifyExistingListWithQueryParams(List<Theater> baseList, String city, String zipcode);
}
