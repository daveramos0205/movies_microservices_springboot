package com.davidango.useraccounts.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name="USERS")
public class User {
	
	@Id
	@Column(name="ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name="FULL_NAME")
	@NotNull
	private String fullName;
	
	@Column(name="USERNAME")
	@NotNull
	private String userName;
	
	@Column(name="PASSWORD")
	@NotNull
	private String password;
	
	@Column(name="BILLING_ADDR_STREET")
	@NotNull
	private String billingStreetAddress;
	
	@Column(name="CC")  //can be null
	private String creditCardNumber;
	
	@Column(name="EXPIRY")  //can be null
	private String expiryDate;
	
	@Column(name="BILLING_CITY")
	@NotNull
	private String billingCity;
	
	@Column(name="BILLING_STATE")
	@NotNull
	private String billingState;
	
	@Column(name="BILLING_ZIPCODE")
	@NotNull
	private String billingZipcode;
}
