package com.mumbahiz.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.mumbahiz.model.Loans;

@Repository
public interface LoanRepository extends CrudRepository<Loans, Long> {
	
	@PreAuthorize("hasRole('USER')")
	List<Loans> findByCustomerIdOrderByStartDtDesc(long customerId);

}