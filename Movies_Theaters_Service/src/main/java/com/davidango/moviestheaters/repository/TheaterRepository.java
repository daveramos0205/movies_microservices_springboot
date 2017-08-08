package com.davidango.moviestheaters.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.davidango.moviestheaters.entities.Theater;

@Repository("theaterRepository")
public interface TheaterRepository extends CrudRepository<Theater, Integer> {
	
}
