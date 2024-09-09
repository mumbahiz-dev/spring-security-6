package com.mumbahiz.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mumbahiz.model.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {
	
	
}