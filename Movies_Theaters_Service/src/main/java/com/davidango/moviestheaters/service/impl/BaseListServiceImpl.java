package com.davidango.moviestheaters.service.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import com.davidango.error.exception.LookupErrorException;
import com.davidango.moviestheaters.entities.BaseEntity;

public abstract class BaseListServiceImpl<T extends BaseEntity, R extends CrudRepository<T, Integer>> {
	
	@Autowired
	protected R r;
	
	private String entityName;
	
	protected BaseListServiceImpl(String entityName) {
		this.entityName = entityName;
	}
	
	protected List<T> alphabeticallySort(List<T> list) {
		return list.stream().sorted((obj1, obj2) -> obj1.getName().compareTo(obj2.getName()))
				.collect(Collectors.toList());
	}
	
	protected List<T> getAllAvailable() {
		 return (List<T>) r.findAll();
	}
	
	public T getById(String id) throws LookupErrorException {
		
		Integer entityId;
		
		try {
			entityId = Integer.parseInt(id);
		} 
		catch (NumberFormatException e) {
			throw new LookupErrorException("id must be integer");
		}
		
		T dbResult = r.findOne(entityId);
		
		if (dbResult == null)
			throw new LookupErrorException("Unable to find entity " + entityName + " with id " + entityId);
		else
			return dbResult;
	}
	
	protected List<T> processListWithName(String expectedName, List<T> list) {
		return alphabeticallySort(list.stream().filter(obj -> obj.getName().toLowerCase().contains(expectedName.toLowerCase()))
				.collect(Collectors.toList()));
	}
	
	protected List<T> processListWithEnum(String enumName, 
										  String[] enumValues, 
										  String expectedEnumValue, 
										  String methodName, 
										  List<T> baseList) throws LookupErrorException {
		if (!Arrays.stream(enumValues).anyMatch(expectedEnumValue::equals)) 	//not found
			throw new LookupErrorException("query parameter " + enumName + "=" + expectedEnumValue + " must be in " + Arrays.toString(enumValues));
	
		return processList(baseList, methodName, expectedEnumValue);
	}
	
	protected List<T> processList(List<T> list, String methodName, String expectedValue) {
		return alphabeticallySort(list.stream().filter(obj -> valueMatches(obj, methodName, expectedValue)).collect(Collectors.toList()));
	}
	
	private boolean valueMatches(Object obj, String methodName, String expectedValue) {
		Method method = null;
		try {
			method = obj.getClass().getMethod(methodName);
		} catch (NoSuchMethodException e) {} //squelched
		
		boolean result = false;
		try {
			result = method.invoke(obj).toString().equals(expectedValue);
		} catch (Exception e) {}  //all checked exceptions squelched; internal
		return result;
	}
}
