package org.openpcm.service;

import java.util.List;
import java.util.Optional;

import org.openpcm.dao.UserRepository;
import org.openpcm.exceptions.DataViolationException;
import org.openpcm.exceptions.NotFoundException;
import org.openpcm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;

@Transactional
@Service
public class UserService {

    private final UserRepository patientRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User patient) throws DataViolationException {
        if (patient.getId() != null && patient.getId() != 0) {
            throw new DataViolationException("patient id should be null on create");
        }

        if (patient.getAddress() == null && (patient.getAddress().getId() != null && patient.getAddress().getId() != 0)) {
            throw new DataViolationException("address id should be null on create");
        }

        if (!StringUtils.isEmpty(patient.getPassword())) {
            patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        }

        return patientRepository.save(patient);
    }

    public User read(Long id) throws NotFoundException {
        Optional<User> patient = patientRepository.findById(id);

        if (patient.isPresent()) {
            return patient.get();
        } else {
            throw new NotFoundException(id + " not found");
        }
    }

    public List<User> readAll() {
        Iterable<User> patientIter = patientRepository.findAll();

        return Lists.newArrayList(patientIter);
    }

    public Page<User> pageReadAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    public User update(Long id, User patient) throws NotFoundException {
        Optional<User> dbPatient = patientRepository.findById(id);

        if (!dbPatient.isPresent()) {
            throw new NotFoundException(id + " not found");
        }

        // detecting if password changed, if it did we encrypt it because it's raw password
        if (!StringUtils.isEmpty(patient.getPassword())) {
            if (!patient.getPassword().equals(dbPatient.get().getPassword())) {
                patient.setPassword(passwordEncoder.encode(patient.getPassword()));
            }
        }

        patient.setId(dbPatient.get().getId());

        return patientRepository.save(patient);
    }

    public void delete(Long id) {
        patientRepository.deleteById(id);
    }
}
