package com.davidango.useraccounts.http.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.davidango.useraccounts.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpUser {

    //cannot have an ID here, so separate class is needed
	@NotNull
	@Size(min=5)
	private String fullName;

	@NotNull
	@Size(min=8)
	private String userName;
	
	@NotNull
	@Size(min=8)
	private String password;
	
	@NotNull
	@Size(min=8)
	private String streetAddress;
	
	@Size(min=16, max=16)   //enforce only digits
	private String creditCardNumber;
	
	@Size(min=7, max=7)   //MM-yyyy 
	private String expiryDate;
	
	@NotNull
	@Size(min=2)
	private String city;
	
	@NotNull
	@Size(min=2, max=2)   //must be abbr
	private String state;
	
	@NotNull
	@Size(min=5, max=5)   //must be 5 digits, more validations later
	private String zipcode;
	
	
	public static HttpUser convert(User user) {	
		return HttpUser.builder()
			.fullName(user.getFullName())
			.userName(user.getUserName())   //password is not repeated
			.streetAddress(user.getBillingStreetAddress())
			.creditCardNumber(user.getCreditCardNumber())
			.expiryDate(user.getExpiryDate())
			.city(user.getBillingCity())
			.state(user.getBillingState())
			.zipcode(user.getBillingZipcode())
				.build();
	}
}
