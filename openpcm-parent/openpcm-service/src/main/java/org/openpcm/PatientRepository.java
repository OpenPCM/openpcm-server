package org.openpcm;

import java.util.List;

import org.openpcm.model.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepository extends CrudRepository<Patient, Long>{

	public List<Patient> findByFirstNameAndLastName(String firstName, String lastName);
}
