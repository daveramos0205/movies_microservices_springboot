package com.davidango.moviestheaters.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
@Entity
@Table(name="MOVIE_SHOWTIMES")
@JsonIgnoreProperties("id")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieShowTimes {
	
	@Id
	@Column(name="ID")
	private Integer id;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MOV_ID")
	private Movie movie;
	
	@Column(name="CLOSING_DATE")
	@NotNull
	private String closingDate;
	
	@Column(name="SHOWTIMES")
	@NotNull
	private String showTimes;
	

	
	@Override
	public String toString() {
		
		return "MOVIESHOWTIMES:id="+id+",closingDate="+closingDate+",showTimes="+showTimes+";"+
				movie == null ? "" : movie.toString();
	}
}
