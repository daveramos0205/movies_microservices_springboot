package com.davidango.useraccounts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.error.exception.UserCreationErrorException;
import com.davidango.useraccounts.entities.User;
import com.davidango.useraccounts.externalresources.MoviesTheatersServiceClient;
import com.davidango.useraccounts.http.entities.HttpUser;
import com.davidango.useraccounts.externalresources.Theater;
import com.davidango.useraccounts.repository.UsersRepository;
import com.davidango.useraccounts.service.UsersService;

import static org.springframework.util.StringUtils.isEmpty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service("usersService")
public class UsersServiceImpl implements UsersService {
	
	public static final String OBFUSCATION_FIELD = "XXXXXXXXXX";
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private MoviesTheatersServiceClient moviesTheatersclient;
	
	@Override
	public HttpUser createAccount(HttpUser newAccount) throws UserCreationErrorException{
		
		if (newAccount == null)
			throw new UserCreationErrorException("request body is null");  //should take care of empty body
		
		if (StringUtils.containsWhitespace(newAccount.getUserName()))		
			throw new UserCreationErrorException("username must not have spaces");
	
		
		if (!isEmpty(newAccount.getCreditCardNumber()))
			if (isEmpty(newAccount.getExpiryDate()))
				throw new UserCreationErrorException("Need to specify both CC and Expiry");
		
		
		if (!isEmpty(newAccount.getExpiryDate()))
			if (isEmpty(newAccount.getCreditCardNumber()))
				throw new UserCreationErrorException("Need to specify both CC and Expiry");
		
		if (newAccount.getCreditCardNumber() != null) {
		
			if (!newAccount.getCreditCardNumber().matches("[0-9]+"))
				throw new UserCreationErrorException("CC must be all digits");
		
		
			if (!newAccount.getExpiryDate().matches("[0-9]{2}-[0-9]{4}"))
				throw new UserCreationErrorException("expiry date must be in MM-yyyy format only");
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
			sdf.setLenient(false);
			try {
				sdf.parse(newAccount.getExpiryDate());
			} catch (ParseException e) {
				throw new UserCreationErrorException("expiry date must be in MM-yyyy format only");
			}
		}
	
	
		
		if (!STATES_ABBR.values().contains(newAccount.getState()))
				throw new UserCreationErrorException("state field must be valid US state abbr");
		
		
		if (!newAccount.getZipcode().matches("[0-9]+"))
			throw new UserCreationErrorException("zip code must be 5 digits");
		
		User user = User.builder()
			.fullName(newAccount.getFullName())
			.userName(newAccount.getUserName())
			.password(newAccount.getPassword())
			.billingStreetAddress(newAccount.getStreetAddress())
			.billingCity(newAccount.getCity())
			.billingState(newAccount.getState())
			.billingZipcode(newAccount.getZipcode())
			.creditCardNumber(newAccount.getCreditCardNumber())
			.expiryDate(newAccount.getExpiryDate())
			.build();
		
		try {
			usersRepository.save(user);
		} catch (Exception e) {    //might be SQLException for duplicate username? check
			throw new UserCreationErrorException(e.getMessage());
		}
		newAccount.setPassword(OBFUSCATION_FIELD);
		newAccount.setCreditCardNumber(OBFUSCATION_FIELD);
		newAccount.setExpiryDate(OBFUSCATION_FIELD);
		
		return newAccount;
	}

	@Override
	public HttpUser getByUserName(String userName) throws LookupErrorException {
		if (userName.isEmpty())
			throw new LookupErrorException("userName cannot be empty");
		
		User user = usersRepository.getByUserName(userName);
		
		if (user == null)
			throw new LookupErrorException("userName " + userName + " was not found");
		
		HttpUser httpUser = HttpUser.convert(user);
		
		//obfuscate certain fields
		httpUser.setPassword(OBFUSCATION_FIELD);
		httpUser.setCreditCardNumber(OBFUSCATION_FIELD);
		httpUser.setExpiryDate(OBFUSCATION_FIELD);
		
		return httpUser;
	}
	
	@Override
	public List<Theater> getTodaysMoviesByUserCity(String userName) throws LookupErrorException {
		HttpUser user = getByUserName(userName);
		String userCity = user.getCity();
		return moviesTheatersclient.getTheatersShowtimesByCity(userCity);		
	}

	@Override
	public List<Theater> getTodaysMoviesByUserZipcode(String userName) throws LookupErrorException {
		HttpUser user = getByUserName(userName);
		String userZipcode = user.getZipcode();
		return moviesTheatersclient.getTheatersShowtimesByZipcode(userZipcode);
	}
	
	
	//https://stackoverflow.com/questions/11005751/is-there-a-util-to-convert-us-state-name-to-state-code-eg-arizona-to-az
	private static final Map<String, String> STATES_ABBR = new LinkedHashMap<String, String>();
	
	static {
		STATES_ABBR.put("Alabama","AL");
		STATES_ABBR.put("Alaska","AK");
		STATES_ABBR.put("Alberta","AB");
		STATES_ABBR.put("American Samoa","AS");
		STATES_ABBR.put("Arizona","AZ");
		STATES_ABBR.put("Arkansas","AR");
		STATES_ABBR.put("Armed Forces (AE)","AE");
		STATES_ABBR.put("Armed Forces Americas","AA");
		STATES_ABBR.put("Armed Forces Pacific","AP");
		STATES_ABBR.put("British Columbia","BC");
		STATES_ABBR.put("California","CA");
		STATES_ABBR.put("Colorado","CO");
		STATES_ABBR.put("Connecticut","CT");
		STATES_ABBR.put("Delaware","DE");
		STATES_ABBR.put("District Of Columbia","DC");
		STATES_ABBR.put("Florida","FL");
		STATES_ABBR.put("Georgia","GA");
		STATES_ABBR.put("Guam","GU");
		STATES_ABBR.put("Hawaii","HI");
		STATES_ABBR.put("Idaho","ID");
		STATES_ABBR.put("Illinois","IL");
		STATES_ABBR.put("Indiana","IN");
		STATES_ABBR.put("Iowa","IA");
		STATES_ABBR.put("Kansas","KS");
		STATES_ABBR.put("Kentucky","KY");
		STATES_ABBR.put("Louisiana","LA");
		STATES_ABBR.put("Maine","ME");
		STATES_ABBR.put("Manitoba","MB");
		STATES_ABBR.put("Maryland","MD");
		STATES_ABBR.put("Massachusetts","MA");
		STATES_ABBR.put("Michigan","MI");
		STATES_ABBR.put("Minnesota","MN");
		STATES_ABBR.put("Mississippi","MS");
		STATES_ABBR.put("Missouri","MO");
		STATES_ABBR.put("Montana","MT");
		STATES_ABBR.put("Nebraska","NE");
		STATES_ABBR.put("Nevada","NV");
		STATES_ABBR.put("New Brunswick","NB");
		STATES_ABBR.put("New Hampshire","NH");
		STATES_ABBR.put("New Jersey","NJ");
		STATES_ABBR.put("New Mexico","NM");
		STATES_ABBR.put("New York","NY");
		STATES_ABBR.put("Newfoundland","NF");
		STATES_ABBR.put("North Carolina","NC");
		STATES_ABBR.put("North Dakota","ND");
		STATES_ABBR.put("Northwest Territories","NT");
		STATES_ABBR.put("Nova Scotia","NS");
		STATES_ABBR.put("Nunavut","NU");
		STATES_ABBR.put("Ohio","OH");
		STATES_ABBR.put("Oklahoma","OK");
		STATES_ABBR.put("Ontario","ON");
		STATES_ABBR.put("Oregon","OR");
		STATES_ABBR.put("Pennsylvania","PA");
		STATES_ABBR.put("Prince Edward Island","PE");
		STATES_ABBR.put("Puerto Rico","PR");
		STATES_ABBR.put("Quebec","QC");
		STATES_ABBR.put("Rhode Island","RI");
		STATES_ABBR.put("Saskatchewan","SK");
		STATES_ABBR.put("South Carolina","SC");
		STATES_ABBR.put("South Dakota","SD");
		STATES_ABBR.put("Tennessee","TN");
		STATES_ABBR.put("Texas","TX");
		STATES_ABBR.put("Utah","UT");
		STATES_ABBR.put("Vermont","VT");
		STATES_ABBR.put("Virgin Islands","VI");
		STATES_ABBR.put("Virginia","VA");
		STATES_ABBR.put("Washington","WA");
		STATES_ABBR.put("West Virginia","WV");
		STATES_ABBR.put("Wisconsin","WI");
		STATES_ABBR.put("Wyoming","WY");
		STATES_ABBR.put("Yukon Territory","YT");
	}
}	
