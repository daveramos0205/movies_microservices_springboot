package com.davidango.useraccounts;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.davidango.useraccounts.entities.HttpUser;
/**
 * Suite of Rest Assured tests
 * @author dave.ramos
 */
public class TestUsersAccountsResource {

	private static final String HOST = "http://localhost:8090/internal/v1/api/";
	private static final String GET_USER_URL = "getByUserName/{userName}";
	private static final String CREATE_USER_URL = "createUser";

	@Test
	public void testGettingNonExistentUser() {
		
		given().log().all()
		.when().get(HOST + GET_USER_URL, "davidshmavid66678888")  //non-existent user in this system
		.then().log().all().statusCode(400).body("errorCode", equalTo(1))
				.body("errorMessage", equalTo("userName davidshmavid66678888 was not found"));

	}
	
	@Test
	public void testGetExistingUser() {  //user exists, happy get user case
		given().log().all()
		.when().get(HOST + GET_USER_URL, "davidshmavid666") 
		.then().log().all().statusCode(200).body("fullName", equalTo("David Ramos"))
				.body("userName", equalTo("davidshmavid666"));

	}
	
	@Test
	public void testFailureCreateWithDuplicateUserName() {
		HttpUser user = new HttpUser();
		user.fullName = "DavidRamos";
		user.userName = "davidshmavid666";  //already exists in the system
		user.password = "passw0rt";
		user.streetAddress = "123 Main Street";
		user.city = "San Jose";
		user.state = "CA";
		user.zipcode = "95125";
		
        given()
        .contentType("application/json")
        .body(user)
        .when().post(HOST + CREATE_USER_URL).then()
        .body("errorMessage", containsString("ConstraintViolationException") ).statusCode(400);
	}
	
	@Test
	public void testFailureCreateWithWhiteSpaceInUserName() {
		HttpUser user = new HttpUser();
		user.fullName = "DavidRamos";
		user.userName = "davidshmavid666 ";  //whitespace found here, none allowed
		user.password = "passw0rt";
		user.streetAddress = "123 Main Street";
		user.city = "San Jose";
		user.state = "CA";
		user.zipcode = "95125";
		
        given()
        .contentType("application/json")
        .body(user)
        .when().post(HOST + CREATE_USER_URL).then()
        .body("errorMessage", containsString("username must not have spaces")).statusCode(400);
	}
	
	
	@Test
	public void testFailurePasswordNotMinimumLength() {
		HttpUser user = new HttpUser();
		user.fullName = "DavidRamos";
		user.userName = "davidshmavid666 ";
		user.password = "pas";   //not correct length for password
		user.streetAddress = "123 Main Street";
		user.city = "San Jose";
		user.state = "CA";
		user.zipcode = "95125";
		
        given()
        .contentType("application/json")
        .body(user)
        .when().post(HOST + CREATE_USER_URL).then()
        .body("errorMessage", containsString("size must be between 8 and 2147483647")).statusCode(400);
	}
	
	@Test
	public void testFailureExpiryDateMissingWhenCCIsProvided() {
		HttpUser user = new HttpUser();
		user.fullName = "DavidRamos";
		user.userName = "davidshmavid666234523454";
		user.password = "passw0rt";  
		user.streetAddress = "123 Main Street";
		user.city = "San Jose";
		user.state = "CA";
		user.zipcode = "95125";
		user.creditCardNumber = "1234567891234567";   //only provided cc here
		
        given()
        .contentType("application/json")
        .body(user)
        .when().post(HOST + CREATE_USER_URL).then()
        .body("errorMessage", containsString("Need to specify both CC and Expiry")).statusCode(400);
	}
	
	@Test
	public void testFailureBadCCFormatProvided() {
		HttpUser user = new HttpUser();
		user.fullName = "DavidRamos";
		user.userName = "davidshmavid666234523454";
		user.password = "passw0rt";  
		user.streetAddress = "123 Main Street";
		user.city = "San Jose";
		user.state = "CA";
		user.zipcode = "95125";
		user.creditCardNumber = "123456789123456x";  //bad cc format
		user.expiryDate = "12-2018";
		
        given()
        .contentType("application/json")
        .body(user)
        .when().post(HOST + CREATE_USER_URL).then()
        .body("errorMessage", containsString("CC must be all digits")).statusCode(400);
	}
	
	@Test
	public void testFailureBadExpiryDateFormat() {
		HttpUser user = new HttpUser();
		user.fullName = "DavidRamos";
		user.userName = "davidshmavid666234523454";
		user.password = "passw0rt";   
		user.streetAddress = "123 Main Street";
		user.city = "San Jose";
		user.state = "CA";
		user.zipcode = "95125";
		user.creditCardNumber = "1234567891234567";
		user.expiryDate = "12-201x";  //bad expiry date format
		
        given()
        .contentType("application/json")
        .body(user)
        .when().post(HOST + CREATE_USER_URL).then()
        .body("errorMessage", containsString("expiry date must be in MM-yyyy format only")).statusCode(400);
	}
	
	@Test
	public void testSuccessCreatingUser() {
		HttpUser user = new HttpUser();
		user.fullName = "DavidRamos";
		user.userName = "davidshmavid666234" + String.valueOf(System.nanoTime()).substring(0, 5);
		user.password = "passw0rt";   
		user.streetAddress = "123 Main Street";
		user.city = "San Jose";
		user.state = "CA";
		user.zipcode = "95125";
		user.creditCardNumber = "1234567891234567";
		user.expiryDate = "12-2018";
		
		HttpUser createdUser = 
				given().log().all().contentType("application/json").body(user)
				.when().post(HOST + CREATE_USER_URL)
				.as(HttpUser.class);
				
		assertThat(createdUser.fullName).isEqualTo("DavidRamos");
		assertThat(createdUser.userName).isEqualTo("davidshmavid666234523454");
		assertThat(createdUser.password).isEqualTo("XXXXXXXXXX");   //obfuscated
	}	
}