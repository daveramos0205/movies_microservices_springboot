package com.davidango.useraccounts;

import static org.assertj.core.api.Assertions.assertThat;
import static com.davidango.useraccounts.service.impl.UsersServiceImpl.OBFUSCATION_FIELD;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.error.exception.UserCreationErrorException;
import com.davidango.useraccounts.http.entities.HttpUser;
import com.davidango.useraccounts.service.UsersService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUsersServiceWithDB {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UsersService usersService;
	
	@Test
	public void addAndGetAccount() throws UserCreationErrorException, LookupErrorException {
		
		String uniqueUserName = "davidshmavid666234" + String.valueOf(System.nanoTime()).substring(0, 5);
		
		HttpUser user = HttpUser.builder()
			.fullName("Davdai Ramosss")
			.userName(uniqueUserName)
			.streetAddress("123 Spring Street")
			.city("San Jose")
			.state("CA")
			.zipcode("95125")
			.creditCardNumber("1234567887654321")
			.expiryDate("12-2018")
			.password("passw0rt")
			.build();
			
		HttpUser createdUser = usersService.createAccount(user);
		logger.info("account added "+ createdUser);
		
		assertThat(createdUser.getUserName()).isEqualTo(uniqueUserName);
		assertThat(createdUser.getPassword()).isEqualTo(OBFUSCATION_FIELD);

		//use uniqueUserName
		HttpUser retrievedUser = usersService.getByUserName(createdUser.getUserName());
		assertThat(retrievedUser.getUserName()).isEqualTo(createdUser.getUserName());
		assertThat(retrievedUser.getPassword()).isEqualTo(OBFUSCATION_FIELD);
	}
}
