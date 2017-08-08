package com.davidango.useraccounts.externalresources;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;


@Component
public class MoviesTheatersServiceResponseErrorHandler extends DefaultResponseErrorHandler {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		logger.info("exception thrown from MoviesTheatersServiceClient: " + response.getStatusText());
	}
}
