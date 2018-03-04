package org.openpcm.controller;

import org.openpcm.exceptions.DataViolationException;
import org.openpcm.model.Patient;
import org.openpcm.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="/api/v1/patient")
@RequestMapping("/api/v1/patient")
@RestController
public class PatientController extends BaseController{

	private final PatientService patientService;
	
	@Autowired
	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value="", response=Patient.class)
	@RequestMapping(value ="", method=RequestMethod.POST)
	public @ResponseBody Patient createPatient(Patient patient) throws DataViolationException {
		return patientService.create(patient);
	}
	
	
}
