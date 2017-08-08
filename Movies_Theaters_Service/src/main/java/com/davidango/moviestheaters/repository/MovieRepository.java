package com.davidango.moviestheaters.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.davidango.moviestheaters.entities.Movie;

@Repository("movieRepository")
public interface MovieRepository extends CrudRepository<Movie, Integer>{

}
