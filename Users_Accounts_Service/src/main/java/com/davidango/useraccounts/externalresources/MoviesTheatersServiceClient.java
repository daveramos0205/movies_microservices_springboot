package com.davidango.useraccounts.externalresources;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.davidango.error.exception.LookupErrorException;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@Component
public class MoviesTheatersServiceClient {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String MOVIES_THEATERS_RESOURCE = "showtimes/byMovie/today/location";
	private static final String MOVIES_THEATERS_SERVICE_BASE_PATH = "/internal/v1/api/";
	
	@Autowired
	private RestTemplate moviesTheatersRestTemplate;
	
	@Autowired
	private EurekaClient discoveryClient;
	
	private String fullUrl;
	
	
	public List<Theater> getTheatersShowtimesByCity(String city) throws LookupErrorException {
		checkMoviesTheatersServiceAvailability();
		return getTheatersShowtimesByParam("?city=" + city);
	}
	
	public List<Theater> getTheatersShowtimesByZipcode(String zipcode) throws LookupErrorException {
		checkMoviesTheatersServiceAvailability();
		return getTheatersShowtimesByParam("?zipcode=" + zipcode);
	}
	
	private List<Theater> getTheatersShowtimesByParam(String parameter) throws LookupErrorException{
		ResponseEntity<Theater[]> results = moviesTheatersRestTemplate.getForEntity(
				fullUrl + parameter, Theater[].class);
		
		HttpStatus statusCode = results.getStatusCode();
		logger.info("received HttpStatus from MoviesTheatersServiceClient: " + statusCode.toString());
		if (!statusCode.equals(HttpStatus.OK))
			throw new LookupErrorException("bad status received from MoviesTheatersServiceClient. Check config");
		
		Theater[] theaters = results.getBody();
		return Arrays.asList(theaters);
	}
	
	
	private void checkMoviesTheatersServiceAvailability() throws LookupErrorException {
		Optional<Application> found = discoveryClient.getApplications()
						.getRegisteredApplications().stream()
						.filter(app -> app.getName().equals("MOVIES-THEATERS-SERVICE")).findFirst();
		
		Application application;
		if (found.isPresent())
			application = found.get();
		else
			throw new LookupErrorException("cannot complete this command as MOVIES_THEATERS_SERVICE is down/unavailable");
		
		InstanceInfo instance = application.getInstances().get(0);

		if (!instance.getStatus().equals(InstanceStatus.UP))
			throw new LookupErrorException("cannot complete this command as MOVIES_THEATERS_SERVICE is down/unavailable");
		
		this.fullUrl = "http://MOVIES-THEATERS-SERVICE" + MOVIES_THEATERS_SERVICE_BASE_PATH + MOVIES_THEATERS_RESOURCE;
	}
}