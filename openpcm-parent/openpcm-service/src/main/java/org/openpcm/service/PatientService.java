package org.openpcm.service;

import java.util.List;
import java.util.Optional;

import org.openpcm.dao.PatientRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Transactional
@Service
public class PatientService {

	private final PatientRepository patientRepository;

	@Autowired
	public PatientService(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	public Patient create(Patient patient) throws DataViolationException {
		if(patient.getId() != null && patient.getId() != 0) {
			throw new DataViolationException("patient id should be null on create");
		}
		
		if(patient.getAddress() == null && (patient.getAddress().getId() != null && patient.getAddress().getId() != 0)) {
			throw new DataViolationException("address id should be null on create");
		}
		
		return patientRepository.save(patient);
	}

	public Patient read(Long id) throws NotFoundException {
		Optional<Patient> patient = patientRepository.findById(id);

		if (patient.isPresent()) {
			return patient.get();
		} else {
			throw new NotFoundException(id + " not found");
		}
	}
	
	public List<Patient> readAll() {
		Iterable<Patient> patientIter = patientRepository.findAll();
		
		return Lists.newArrayList(patientIter);
	}
	
	public Page<Patient> pageReadAll(Pageable pageable) {
		return patientRepository.findAll(pageable);
	}
	
	public Patient update(Long id, Patient patient) throws NotFoundException {
		Optional<Patient> dbPatient = patientRepository.findById(id);
		
		if(!dbPatient.isPresent()) {
			throw new NotFoundException(id + " not found");
		}
		
		patient.setId(dbPatient.get().getId());
		
		return patientRepository.save(patient);
	}
	
	public void delete(Long id) {
		patientRepository.deleteById(id);
	}
}
