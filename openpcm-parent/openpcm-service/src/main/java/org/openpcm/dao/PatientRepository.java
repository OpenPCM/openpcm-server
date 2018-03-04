package org.openpcm.dao;

import java.util.List;

import org.openpcm.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PatientRepository extends PagingAndSortingRepository<Patient, Long>{
	
	Patient findBySsn(String ssn);
	
	List<Patient> findByFirstNameContainingAndLastNameContaining(String firstName, String lastName);
	
	Page<Patient> findAll(Pageable pageable);
	
	public List<Patient> findByFirstNameAndLastName(String firstName, String lastName);

}
