package com.davidango.useraccounts.externalresources;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Theater{
	
	private Integer id;
	private String name;
	private String city;
	private String state;
	private String zipcode;
	
	private List<MovieShowTimes> movieShowTimes;
	
	@Override
	public String toString() {
		String one = "THEATER:id="+id+",name="+name+",city="+city+",state="+state+",zipcode="+zipcode+";";
		
		for(MovieShowTimes mst: movieShowTimes)
			if (mst != null)
				one += mst.toString();
		
		return one;
	}
}

