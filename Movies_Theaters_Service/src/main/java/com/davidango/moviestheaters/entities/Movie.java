package com.davidango.moviestheaters.entities;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="MOVIES")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie extends BaseEntity{

	@Id
	@Column(name="MOVIE_ID")
	private Integer id;
	
	@Column(name="NAME")
	@NotNull
	private String name;
	
	@Column(name="GENRE")   
	@NotNull
	private String genre;
	
	@Column(name="OPENING_DATE")
	@NotNull
	private String openingDate;
	
	@Column(name="RATING")
	@NotNull
	private String rating;
	

	public enum Genre {
		horror, scifi, comedy, drama, kids;
		
		public static String[] getStringNames() {
			return Arrays.stream(Movie.Genre.values()).map(i -> i.name()).collect(Collectors.toList())
			.stream().toArray(String[]::new);
		}
	}
	

	public enum Rating {
		g, pg, pg13, r;
		
		public static String[] getStringNames() {
			return Arrays.stream(Movie.Rating.values()).map(i -> i.name()).collect(Collectors.toList())
					.stream().toArray(String[]::new);
		}
	}
	
	@Override
	public String toString() {
		return "MOVIE:id="+id+",name="+name+",genre="+genre+",openingDate="+openingDate+",rating="+rating+";";
	}
}
