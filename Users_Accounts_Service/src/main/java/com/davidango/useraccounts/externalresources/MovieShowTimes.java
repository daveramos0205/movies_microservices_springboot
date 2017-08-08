package com.davidango.useraccounts.externalresources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieShowTimes {
	
	private Movie movie;
	private String closingDate;
	private String showTimes;
	
	@Override
	public String toString() {
		
		return "MOVIESHOWTIMES:closingDate="+closingDate+",showTimes="+showTimes+";"+
				movie == null ? "" : movie.toString();
	}
}
