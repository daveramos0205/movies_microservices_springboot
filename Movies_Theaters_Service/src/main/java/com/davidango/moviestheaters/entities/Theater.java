package com.davidango.moviestheaters.entities;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="THEATERS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Theater extends BaseEntity{
	
	@Id
	@Column(name="THEATER_ID")
	private Integer id;
	
	@Column(name="NAME")
	@NonNull
	private String name;
	
	@Column(name="CITY")
	@NonNull
	private String city;
	
	@Column(name="STATE")
	@NonNull
	private String state;
	
	@Column(name="ZIPCODE")
	@NonNull
	private String zipcode;
	
	@ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name="THEATER_MOVIESHOWTIMES_MAP", joinColumns=@JoinColumn(name="THEATER_ID"),
		inverseJoinColumns=@JoinColumn(name="MOVIE_SHOWTIMES_ID"))
	private List<MovieShowTimes> movieShowTimes;
	
	@Override
	public String toString(){
		String one = "THEATER:id="+id+",name="+name+",city="+city+",state="+state+",zipcode="+zipcode+";";
		
		for(MovieShowTimes mst: movieShowTimes)
			if (mst != null)
				one += mst.toString();
		
		return one;
	}

	public enum States {
		
		AL("Alabama"),
		AK("Alaska"),
		AZ("Arizona"),
		AR("Arkansas"),
		CA("California");
		
		String fullName;
		States(String fullName) {
			this.fullName = fullName;
		}
		
		public static String[] getStringNames() {
			return Arrays.stream(Theater.States.values()).map(i -> i.name()).collect(Collectors.toList())
			.stream().toArray(String[]::new);
		}
	}
}

