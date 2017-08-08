package com.davidango.useraccounts.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.davidango.useraccounts.entities.User;

@Repository("usersRepository")
@Transactional
public interface UsersRepository extends CrudRepository<User, Integer> {
	
	User getByUserName(String userName);
}
