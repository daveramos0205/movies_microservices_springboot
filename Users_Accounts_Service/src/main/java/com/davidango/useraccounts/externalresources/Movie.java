package com.davidango.useraccounts.externalresources;

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
public class Movie{

	private Integer id;
	private String name;
	private String genre;
	private String openingDate;
	private String rating;

	@Override
	public String toString() {
		return "MOVIE:id="+id+",name="+name+",genre="+genre+",openingDate="+openingDate+",rating="+rating+";";
	}
}
